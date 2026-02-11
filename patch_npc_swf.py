#!/usr/bin/env python3
"""
Patches npc_fr_undefined.swf to add NPC names for profession teachers.
Adds entries to N.n (names), N.a (response texts), and N.d (NPC data).
"""
import zlib, struct, os, shutil

# NPC definitions: (npc_template_id, name, response_id)
NPCS = [
    # Row 1 - Gathering
    (2000, "Maitre Paysan", 8000),
    (2001, "Maitre Bucheron", 8001),
    (2002, "Maitre Pecheur", 8002),
    (2003, "Maitre Chasseur", 8003),
    (2004, "Maitre Alchimiste", 8004),
    (2005, "Maitre Mineur", 8005),
    (2006, "Maitre Chasseur+", 8006),
    # Row 2 - Crafting basic
    (2007, "Maitre Bijoutier", 8007),
    (2008, "Maitre Cordonnier", 8008),
    (2009, "Maitre Tailleur", 8009),
    (2010, "Maitre Boulanger", 8010),
    (2011, "Maitre Boucher", 8011),
    (2012, "Maitre Poissonnier", 8012),
    (2013, "Maitre Cuisinier", 8013),
    # Row 3 - Crafting advanced
    (2014, "Maitre Bricoleur", 8014),
    (2015, "Maitre Forgeur Boucliers", 8015),
    (2016, "Maitre Sculpteur Bois", 8016),
    (2017, "Maitre Forgeur Haches", 8017),
    (2018, "Maitre Forgeur Special", 8018),
    (2019, "Maitre Sculpteur Spec", 8019),
    (2020, "Maitre Sculpteur Baguettes", 8020),
    # Row 4 - Sculpting + FM
    (2021, "Maitre Sculpteur Batons", 8021),
    (2022, "FM Cordonnier", 8022),
    (2023, "FM Bijoutier", 8023),
    (2024, "FM Tailleur", 8024),
    (2025, "FM Boulanger", 8025),
    (2026, "FM Boucher", 8026),
    (2027, "FM Poissonnier", 8027),
    # Row 5 - FM rest
    (2028, "FM Cuisinier", 8028),
    (2029, "FM Bricoleur", 8029),
    (2030, "FM Sculpteur", 8030),
    (2031, "FM Forgeur", 8031),
    (2032, "FM Forgeur 2", 8032),
]

RESPONSE_TEXT = "Apprendre"


def make_push_string(s):
    """Create push data for a string value."""
    encoded = s.encode('utf-8') + b'\x00'
    return b'\x00' + encoded


def make_push_int(val):
    """Create push data for an integer value."""
    return b'\x07' + struct.pack('<I', val)


def make_action_push(push_data_list):
    """Create ActionPush with multiple values."""
    data = b''.join(push_data_list)
    return b'\x96' + struct.pack('<H', len(data)) + data


def make_get_variable():
    return b'\x1c'


def make_get_member():
    return b'\x4e'


def make_set_member():
    return b'\x4f'


def build_bytecode():
    """Build ActionScript bytecode for NPC definitions."""
    bc = b''

    # Add response texts: N.a[responseID] = "Apprendre"
    for npc_id, name, resp_id in NPCS:
        # push "N" → GetVariable → push "a" → GetMember → push responseID, text → SetMember
        bc += make_action_push([make_push_string("N")])
        bc += make_get_variable()
        bc += make_action_push([make_push_string("a")])
        bc += make_get_member()
        bc += make_action_push([make_push_int(resp_id), make_push_string(RESPONSE_TEXT)])
        bc += make_set_member()

    # Add NPC names: N.n[npcID] = "name"
    for npc_id, name, resp_id in NPCS:
        bc += make_action_push([make_push_string("N")])
        bc += make_get_variable()
        bc += make_action_push([make_push_string("n")])
        bc += make_get_member()
        bc += make_action_push([make_push_int(npc_id), make_push_string(name)])
        bc += make_set_member()

    # Add NPC data: N.d[npcID] = {n: "name", a: [responseID]}
    # Pattern from SWF: push npcID, "n", name, "a", responseID, actionCount
    #   InitObject → push arraySize → InitArray → SetMember
    for npc_id, name, resp_id in NPCS:
        bc += make_action_push([make_push_string("N")])
        bc += make_get_variable()
        bc += make_action_push([make_push_string("d")])
        bc += make_get_member()
        # Stack: N.d
        # Now push: npcID, then the object {n: name, a: [resp_id]}
        # InitObject expects pairs on stack: key1, val1, key2, val2, ..., count
        # InitArray expects: elem1, elem2, ..., count
        # We need: N.d[npcID] = {n: name, a: [resp_id]}
        # Push: npcID (key for SetMember)
        # Then build the object:
        #   push "a" (key), then build array [resp_id]: push resp_id, int:1, InitArray
        #   push "n" (key), name (value)
        #   push int:2 (2 properties), InitObject
        bc += make_action_push([make_push_int(npc_id)])
        # Build: push "a", [resp_id array]
        bc += make_action_push([make_push_string("a")])
        bc += make_action_push([make_push_int(resp_id), make_push_int(1)])
        bc += b'\x42'  # InitArray (1 element)
        bc += make_action_push([make_push_string("n"), make_push_string(name)])
        bc += make_action_push([make_push_int(2)])
        bc += b'\x43'  # InitObject (2 properties)
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
    """Read SWF, add new DoAction tag, write patched SWF."""
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

    # Find the End tag (0x0000) and ShowFrame tag
    # We need to insert our DoAction tag before the End tag
    # Parse through the tags to find the insertion point
    pos = 0

    # Skip RECT
    first_byte = data[pos]
    nbits = (first_byte >> 3) & 0x1F
    total_bits = 5 + 4 * nbits
    total_bytes = (total_bits + 7) // 8
    pos += total_bytes

    # Skip frame rate + frame count
    pos += 4

    # Parse tags to find the ShowFrame tag - insert BEFORE it
    tags_start = pos
    show_frame_pos = None
    while pos < len(data):
        tag_start = pos
        tag_header = struct.unpack('<H', data[pos:pos+2])[0]
        tag_type = tag_header >> 6
        tag_length = tag_header & 0x3F
        pos += 2
        if tag_length == 0x3F:
            tag_length = struct.unpack('<I', data[pos:pos+4])[0]
            pos += 4
        if tag_type == 1:  # ShowFrame tag
            show_frame_pos = tag_start
        if tag_type == 0:  # End tag
            break
        pos += tag_length

    # Build new bytecode
    bytecode = build_bytecode()
    new_tag = make_do_action_tag(bytecode)

    # Insert BEFORE ShowFrame tag (DoAction must execute before ShowFrame)
    insertion_point = show_frame_pos if show_frame_pos else pos
    new_data = data[:insertion_point] + new_tag + data[insertion_point:]

    # Calculate new file length (8 bytes header + uncompressed body)
    new_length = 8 + len(new_data)

    # Write compressed SWF
    with open(output_path, 'wb') as f:
        f.write(b'CWS')
        f.write(struct.pack('B', ver))
        f.write(struct.pack('<I', new_length))
        f.write(zlib.compress(new_data))

    print(f"Patched SWF written to {output_path}")
    print(f"Added {len(NPCS)} NPC names, {len(NPCS)} response texts, {len(NPCS)} NPC data entries")
    print(f"New tag size: {len(new_tag)} bytes")


if __name__ == '__main__':
    swf_dir = '/Users/miroslavlalik/DofusEmu/dofus/lang/swf'
    input_file = os.path.join(swf_dir, 'npc_fr_undefined.swf')
    backup_file = os.path.join(swf_dir, 'npc_fr_undefined.swf.bak')
    output_file = os.path.join(swf_dir, 'npc_fr_undefined.swf')

    # Backup original
    if not os.path.exists(backup_file):
        shutil.copy2(input_file, backup_file)
        print(f"Backup created: {backup_file}")

    patch_swf(input_file, output_file)
