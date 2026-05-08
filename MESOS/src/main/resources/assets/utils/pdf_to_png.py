#!/usr/bin/env python3
import argparse
import csv
import os
import shutil
from pathlib import Path

import fitz # PyMuPDF

input_dir = "output"
mapping_file = "mapping.csv"
final_dir = "renamed"
os.makedirs(final_dir, exist_ok=True)

with open(mapping_file, newline='', encoding='utf-8') as f:
    reader = csv.DictReader(f)
    for row in reader:
        page = row['page']
        key = row['imageKey']
        src = os.path.join(input_dir, f"page_{page}.png")
        dst = os.path.join(final_dir, f"{key}.png")
        if os.path.exists(src):
            shutil.copy2(src, dst)