#!/usr/bin/env bash
# Скрипт создаст placeholder PBR текстуры для floor_tile_white
# Требуется ImageMagick (convert).
# Запустите из корня проекта: bash scripts/generate_pbr_placeholders.sh

set -e

OUT_DIR="src/main/resources/assets/fnafmod"
mkdir -p "${OUT_DIR}/textures/pbr/block/floor_tile_white"
mkdir -p "${OUT_DIR}/textures/atlas"

# Цветовая (albedo) — белая
convert -size 512x512 xc:"#FFFFFF" "${OUT_DIR}/textures/pbr/block/floor_tile_white/floor_tile_white_color.png"

# Normal (flat normal) — RGB ~ (128,128,255)
convert -size 512x512 xc:"#8080FF" "${OUT_DIR}/textures/pbr/block/floor_tile_white/floor_tile_white_normal.png"

# Height (mid gray)
convert -size 512x512 xc:"#808080" "${OUT_DIR}/textures/pbr/block/floor_tile_white/floor_tile_white_height.png"

# ORM (R=AO, G=Roughness, B=Metallic) — placeholder: AO=255, Roughness=128, Metallic=0 -> #FF8000
convert -size 512x512 xc:"#FF8000" "${OUT_DIR}/textures/pbr/block/floor_tile_white/floor_tile_white_orm.png"

# Также создаём минимальные атласы (простой дубль same images) — если ваш шейдер ожидает атлас
convert "${OUT_DIR}/textures/pbr/block/floor_tile_white/floor_tile_white_normal.png" -resize 1024x1024 "${OUT_DIR}/textures/atlas/normal.png"
convert "${OUT_DIR}/textures/pbr/block/floor_tile_white/floor_tile_white_height.png" -resize 1024x1024 "${OUT_DIR}/textures/atlas/height.png"

echo "Placeholders created in ${OUT_DIR}/textures/pbr/block/floor_tile_white"