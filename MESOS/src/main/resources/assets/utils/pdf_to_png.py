#!/usr/bin/env python3
import argparse
import csv
import os
import shutil
from pathlib import Path

import fitz  # pymupdf

#usage: python pdf_to_png.py input.pdf mapper.csv --temp-out temp_pages --final-out renamed_png --dpi 300

def read_mapper(csv_path: Path) -> dict[int, str]:
    mapping: dict[int, str] = {}
    with csv_path.open(newline="", encoding="utf-8") as f:
        reader = csv.DictReader(f)
        if "page" not in reader.fieldnames or "imageKey" not in reader.fieldnames:
            raise ValueError("CSV must contain columns: page,imageKey")

        for row in reader:
            page = int(row["page"])
            image_key = row["imageKey"].strip()
            if not image_key:
                raise ValueError(f"Empty imageKey for page {page}")
            mapping[page] = image_key
    return mapping


def export_pdf_to_png(pdf_path: Path, out_dir: Path, dpi: int) -> list[Path]:
    out_dir.mkdir(parents=True, exist_ok=True)

    doc = fitz.open(pdf_path)
    exported: list[Path] = []

    zoom = dpi / 72.0
    matrix = fitz.Matrix(zoom, zoom)

    for i, page in enumerate(doc, start=1):
        pix = page.get_pixmap(matrix=matrix, alpha=False)
        out_path = out_dir / f"page_{i}.png"
        pix.save(str(out_path))
        exported.append(out_path)

    doc.close()
    return exported


def rename_with_mapper(exported_pages: list[Path], mapping: dict[int, str], final_dir: Path):
    final_dir.mkdir(parents=True, exist_ok=True)

    for page_file in exported_pages:
        name = page_file.stem  # page_1
        try:
            page_num = int(name.split("_")[1])
        except (IndexError, ValueError):
            print(f"[WARN] Skipping invalid file name: {page_file.name}")
            continue

        if page_num not in mapping:
            print(f"[WARN] No mapping for page {page_num}, skipping {page_file.name}")
            continue

        image_key = mapping[page_num]
        final_path = final_dir / f"{image_key}.png"

        if final_path.exists():
            print(f"[WARN] Overwriting existing file: {final_path.name}")

        shutil.copy2(page_file, final_path)
        print(f"[OK] {page_file.name} -> {final_path.name}")


def main():
    parser = argparse.ArgumentParser(
        description="Export each PDF page to PNG and rename using a CSV mapper."
    )
    parser.add_argument("pdf", type=Path, help="Input PDF path")
    parser.add_argument("mapper", type=Path, help="CSV mapper path (page,imageKey)")
    parser.add_argument("--temp-out", type=Path, default=Path("tmp_pages"), help="Temporary output folder")
    parser.add_argument("--final-out", type=Path, default=Path("renamed_png"), help="Final output folder")
    parser.add_argument("--dpi", type=int, default=300, help="Export DPI (default: 300)")
    args = parser.parse_args()

    if not args.pdf.exists():
        raise FileNotFoundError(f"PDF not found: {args.pdf}")
    if not args.mapper.exists():
        raise FileNotFoundError(f"Mapper CSV not found: {args.mapper}")

    mapping = read_mapper(args.mapper)
    exported = export_pdf_to_png(args.pdf, args.temp_out, args.dpi)
    rename_with_mapper(exported, mapping, args.final_out)

    print(f"\nDone. Final PNGs are in: {args.final_out.resolve()}")


if __name__ == "__main__":
    main()