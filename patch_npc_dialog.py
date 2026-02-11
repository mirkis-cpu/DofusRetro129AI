#!/usr/bin/env python3
"""
Patches npc_fr_508.swf to add dialog texts for profession teacher NPCs.
Only adds N.a entries (question and response texts).
"""
import zlib, struct, os

# Question texts: N.a[questionID] = "NPC_Name - Profession"
# Response texts: N.a[responseID] = "Apprendre"
DIALOG_TEXTS = [
    # (N.a key, text)
    # Question texts (8000-8032): "NPC Name - Profession"
    (8000, "Zeurg - Farmer"),
    (8001, "Elya Wood - Lumberjack"),
    (8002, "Captain Iglout - Fisherman"),
    (8003, "Harrys Corb - Hunter"),
    (8004, "Casper Van Brushing - Alchemist"),
    (8005, "Abely Bobeule - Miner"),
    (8006, "Hugo Belo - Hunter (Advanced)"),
    (8007, "Vil Smisse - Jeweller"),
    (8008, "Ded Aleicar - Shoemaker"),
    (8009, "Lanseuft de Troille - Tailor"),
    (8010, "Hourax Hipipe - Baker"),
    (8011, "Ale One - Butcher"),
    (8012, "Bellus Sel - Fishmonger"),
    (8013, "Frankie - Cook"),
    (8014, "Lara Soft - Handyman"),
    (8015, "Loopine - Shield Smith"),
    (8016, "Djonz - Wood Carver"),
    (8017, "Lee Aibig - Axe Smith"),
    (8018, "Bolzano - Special Smith"),
    (8019, "Koussein Trengon - Special Carver"),
    (8020, "Eowine Fiole - Wand Carver"),
    (8021, "Maitre Roublard - Staff Carver"),
    (8022, "Wa Wabbit - FM Shoemaker"),
    (8023, "Gawdien Dofus - FM Jeweller"),
    (8024, "Gawdien Dofus - FM Tailor"),
    (8025, "Isse Heau - FM Baker"),
    (8026, "Kanniboul masque - FM Butcher"),
    (8027, "Moon - FM Fishmonger"),
    (8028, "Cherlook Holmus - FM Cook"),
    (8029, "Servante - FM Handyman"),
    (8030, "Kande Boffler - FM Carver"),
    (8031, "Al Okab - FM Smith"),
    (8032, "Alshain - FM Smith 2"),
    # Response texts (9000-9032): all "Learn"
    (9000, "Learn"),
    (9001, "Learn"),
    (9002, "Learn"),
    (9003, "Learn"),
    (9004, "Learn"),
    (9005, "Learn"),
    (9006, "Learn"),
    (9007, "Learn"),
    (9008, "Learn"),
    (9009, "Learn"),
    (9010, "Learn"),
    (9011, "Learn"),
    (9012, "Learn"),
    (9013, "Learn"),
    (9014, "Learn"),
    (9015, "Learn"),
    (9016, "Learn"),
    (9017, "Learn"),
    (9018, "Learn"),
    (9019, "Learn"),
    (9020, "Learn"),
    (9021, "Learn"),
    (9022, "Learn"),
    (9023, "Learn"),
    (9024, "Learn"),
    (9025, "Learn"),
    (9026, "Learn"),
    (9027, "Learn"),
    (9028, "Learn"),
    (9029, "Learn"),
    (9030, "Learn"),
    (9031, "Learn"),
    (9032, "Learn"),
]


def build_bytecode():
    """Build bytecode that only sets N.a entries."""
    bc = b''
    for text_id, text in DIALOG_TEXTS:
        # Push "N" → GetVariable → Push "a" → GetMember → Push id, text → SetMember
        # Using direct string pushes (type 0x00) and integer pushes (type 0x07)
        # Push "N"
        s = "N".encode('utf-8') + b'\x00'
        push_data = b'\x00' + s
        bc += b'\x96' + struct.pack('<H', len(push_data)) + push_data
        # GetVariable
        bc += b'\x1c'
        # Push "a"
        s = "a".encode('utf-8') + b'\x00'
        push_data = b'\x00' + s
        bc += b'\x96' + struct.pack('<H', len(push_data)) + push_data
        # GetMember
        bc += b'\x4e'
        # Push int:id, string:text
        int_data = b'\x07' + struct.pack('<I', text_id)
        str_data = b'\x00' + text.encode('utf-8') + b'\x00'
        push_data = int_data + str_data
        bc += b'\x96' + struct.pack('<H', len(push_data)) + push_data
        # SetMember
        bc += b'\x4f'
    # End
    bc += b'\x00'
    return bc


def make_do_action_tag(bytecode):
    """Create a DoAction SWF tag (type 12)."""
    tag_type = 12
    tag_length = len(bytecode)
    if tag_length < 0x3F:
        header = struct.pack('<H', (tag_type << 6) | tag_length)
    else:
        header = struct.pack('<H', (tag_type << 6) | 0x3F)
        header += struct.pack('<I', tag_length)
    return header + bytecode


def patch_swf(input_path, output_path):
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

    # Parse RECT
    pos = 0
    nbits = (data[pos] >> 3) & 0x1F
    total_bits = 5 + 4 * nbits
    total_bytes = (total_bits + 7) // 8
    pos += total_bytes + 4  # RECT + frame rate + frame count

    # Find ShowFrame tag position
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
        if tag_type == 1:  # ShowFrame
            show_frame_pos = tag_start
        if tag_type == 0:  # End
            break
        pos += tag_length

    # Build and insert new DoAction tag BEFORE ShowFrame
    bytecode = build_bytecode()
    new_tag = make_do_action_tag(bytecode)

    new_data = data[:show_frame_pos] + new_tag + data[show_frame_pos:]
    new_length = 8 + len(new_data)

    with open(output_path, 'wb') as f:
        f.write(b'CWS')
        f.write(struct.pack('B', ver))
        f.write(struct.pack('<I', new_length))
        f.write(zlib.compress(new_data))

    print(f"Patched: {output_path}")
    print(f"Added {len(DIALOG_TEXTS)} N.a text entries ({len(new_tag)} bytes)")


if __name__ == '__main__':
    patch_swf(
        'dofus/lang/swf/npc_fr_508.swf',
        'dofus/lang/swf/npc_fr_509.swf'
    )
