#!/usr/bin/env python3
"""
Patches NPC SWF to add GM Shop NPC definitions (IDs 1235-1253).
Each NPC gets a Buy/Sell action (action ID 1) so the client shows "Buy/Sell" option.

Adds entries to:
  N.n[npcID] = "NPC Name"       (display name)
  N.d[npcID] = {n: "Name", a: [1]}  (NPC data with Buy/Sell action)
"""
import zlib, struct, os, sys

# GM Shop NPC definitions: (template_id, display_name)
SHOP_NPCS = [
    (1235, "GM Hat Shop"),
    (1236, "GM Cape Shop"),
    (1237, "GM Amulet Shop"),
    (1238, "GM Ring Shop"),
    (1239, "GM Belt Shop"),
    (1240, "GM Boot Shop"),
    (1241, "GM Shield Shop"),
    (1242, "GM Sword Shop"),
    (1243, "GM Dagger Shop"),
    (1244, "GM Bow Shop"),
    (1245, "GM Wand Shop"),
    (1246, "GM Staff Shop"),
    (1247, "GM Hammer Shop"),
    (1248, "GM Shovel Shop"),
    (1249, "GM Axe Shop"),
    (1250, "GM Pet Shop"),
    (1251, "GM Dofus Shop"),
    (1252, "GM Potion Shop"),
    (1253, "GM Resource Shop"),
]

# Action ID 1 = "Buy/Sell" in the Dofus 1.29 client
BUY_SELL_ACTION = 1


def make_push_string(s):
    """Create push data for a string value (type 0x00)."""
    return b'\x00' + s.encode('utf-8') + b'\x00'


def make_push_int(val):
    """Create push data for an integer value (type 0x07)."""
    return b'\x07' + struct.pack('<I', val)


def make_action_push(push_data_list):
    """Create ActionPush (0x96) with multiple values."""
    data = b''.join(push_data_list)
    return b'\x96' + struct.pack('<H', len(data)) + data


def make_get_variable():
    return b'\x1c'


def make_get_member():
    return b'\x4e'


def make_set_member():
    return b'\x4f'


def build_bytecode():
    """Build ActionScript bytecode for GM Shop NPC definitions."""
    bc = b''

    # Add NPC names: N.n[npcID] = "name"
    for npc_id, name in SHOP_NPCS:
        bc += make_action_push([make_push_string("N")])
        bc += make_get_variable()
        bc += make_action_push([make_push_string("n")])
        bc += make_get_member()
        bc += make_action_push([make_push_int(npc_id), make_push_string(name)])
        bc += make_set_member()

    # Add NPC data: N.d[npcID] = {n: "name", a: [1]}
    for npc_id, name in SHOP_NPCS:
        bc += make_action_push([make_push_string("N")])
        bc += make_get_variable()
        bc += make_action_push([make_push_string("d")])
        bc += make_get_member()
        # Stack: N.d
        # Push npcID (key for SetMember on N.d)
        bc += make_action_push([make_push_int(npc_id)])
        # Build array [1] for Buy/Sell action
        bc += make_action_push([make_push_string("a")])
        bc += make_action_push([make_push_int(BUY_SELL_ACTION), make_push_int(1)])
        bc += b'\x42'  # InitArray (1 element)
        # Build object {n: name, a: [1]}
        bc += make_action_push([make_push_string("n"), make_push_string(name)])
        bc += make_action_push([make_push_int(2)])
        bc += b'\x43'  # InitObject (2 properties: "n" and "a")
        bc += make_set_member()

    # End action
    bc += b'\x00'
    return bc


def make_do_action_tag(bytecode):
    """Create a DoAction SWF tag (type 12)."""
    tag_type = 12
    tag_length = len(bytecode)
    if tag_length < 0x3F:
        header = struct.pack('<H', (tag_type << 6) | tag_length)
        return header + bytecode
    else:
        header = struct.pack('<H', (tag_type << 6) | 0x3F)
        header += struct.pack('<I', tag_length)
        return header + bytecode


def patch_swf(input_path, output_path):
    """Read SWF, add new DoAction tag with GM Shop NPCs, write patched SWF."""
    with open(input_path, 'rb') as f:
        sig = f.read(3)
        ver = struct.unpack('B', f.read(1))[0]
        file_length = struct.unpack('<I', f.read(4))[0]
        rest = f.read()

    if sig == b'CWS':
        data = zlib.decompress(rest)
    elif sig == b'FWS':
        data = rest
    else:
        raise ValueError(f"Unknown SWF signature: {sig}")

    # Parse RECT header
    pos = 0
    first_byte = data[pos]
    nbits = (first_byte >> 3) & 0x1F
    total_bits = 5 + 4 * nbits
    total_bytes = (total_bits + 7) // 8
    pos += total_bytes

    # Skip frame rate + frame count
    pos += 4

    # Parse tags to find ShowFrame tag - insert BEFORE it
    show_frame_pos = None
    end_pos = None
    while pos < len(data):
        tag_start = pos
        tag_header = struct.unpack('<H', data[pos:pos+2])[0]
        tag_type = tag_header >> 6
        tag_length = tag_header & 0x3F
        pos += 2
        if tag_length == 0x3F:
            tag_length = struct.unpack('<I', data[pos:pos+4])[0]
            pos += 4
        if tag_type == 1:  # ShowFrame
            show_frame_pos = tag_start
        if tag_type == 0:  # End tag
            end_pos = tag_start
            break
        pos += tag_length

    # Build new bytecode and DoAction tag
    bytecode = build_bytecode()
    new_tag = make_do_action_tag(bytecode)

    # Insert BEFORE ShowFrame tag
    insertion_point = show_frame_pos if show_frame_pos else end_pos
    new_data = data[:insertion_point] + new_tag + data[insertion_point:]

    # Calculate new file length
    new_length = 8 + len(new_data)

    # Write compressed SWF
    with open(output_path, 'wb') as f:
        f.write(b'CWS')
        f.write(struct.pack('B', ver))
        f.write(struct.pack('<I', new_length))
        f.write(zlib.compress(new_data))

    print(f"Patched SWF written to {output_path}")
    print(f"Added {len(SHOP_NPCS)} NPC definitions with Buy/Sell action")
    print(f"New DoAction tag size: {len(new_tag)} bytes")


if __name__ == '__main__':
    swf_dir = '/Users/miroslavlalik/DofusEmu/dofus/lang/swf'
    input_file = os.path.join(swf_dir, 'npc_en_517.swf')
    output_file = os.path.join(swf_dir, 'npc_en_518.swf')

    if not os.path.exists(input_file):
        print(f"ERROR: Input file not found: {input_file}")
        sys.exit(1)

    patch_swf(input_file, output_file)
    print(f"\nDone! Now update versions_en.txt: change 'npc,en,517' to 'npc,en,518'")
