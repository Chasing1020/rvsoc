#!/usr/bin/env python3

import os
import glob

cur_dir = os.getcwd()

hex_files = glob.glob(os.path.join(cur_dir, '*.hex'))

for hex_file in hex_files:
    with open(hex_file, "r") as infile:
        lines = infile.readlines()

    with open(hex_file, "w") as outfile:
        for line in lines:
            line = line.strip()
            if len(line) < 8:
                line += "0" * (8 - len(line) % 8)
            outfile.write(line + "\n")

