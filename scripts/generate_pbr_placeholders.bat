:: CMD-батник: scripts\generate_pbr_placeholders.bat
:: Запустите из корня проекта: double-click или в CMD:
::   scripts\generate_pbr_placeholders.bat
@echo off
setlocal

:: Путь вывода (можно изменить)
set "OUT_DIR=src\main\resources\assets\fnafmod"
set "PBR_DIR=%OUT_DIR%\textures\pbr\block\floor_tile_white"
set "ATLAS_DIR=%OUT_DIR%\textures\atlas"

:: Проверка magick
where magick >nul 2>&1
if errorlevel 1 (
    echo ImageMagick (magick.exe) не найден в PATH. Установите ImageMagick и добавьте в PATH.
    PAUSE
    exit /b 1
)

:: Создать папки
mkdir "%PBR_DIR%" 2>nul
mkdir "%ATLAS_DIR%" 2>nul

set size=512
set color="%PBR_DIR%\floor_tile_white_color.png"
set normal="%PBR_DIR%\floor_tile_white_normal.png"
set height="%PBR_DIR%\floor_tile_white_height.png"
set orm="%PBR_DIR%\floor_tile_white_orm.png"
set atlas_normal="%ATLAS_DIR%\normal.png"
set atlas_height="%ATLAS_DIR%\height.png"

echo Creating placeholder textures (%size%x%size%)...

:: color: white
magick -size %size%x%size% xc:"#FFFFFF" -strip -define png:compression-level=9 %color%
:: normal: flat normal (128,128,255)
magick -size %size%x%size% xc:"#8080FF" -strip -define png:compression-level=9 %normal%
:: height: mid gray
magick -size %size%x%size% xc:"#808080" -strip -define png:compression-level=9 %height%
:: orm placeholder: #FF8000
magick -size %size%x%size% xc:"#FF8000" -strip -define png:compression-level=9 %orm%

echo Creating atlas (duplicates)...
magick convert %normal% -resize 1024x1024! -strip -define png:compression-level=9 %atlas_normal%
magick convert %height% -resize 1024x1024! -strip -define png:compression-level=9 %atlas_height%

echo Placeholders created:
echo  - %color%
echo  - %normal%
echo  - %height%
echo  - %orm%
echo  - %atlas_normal%
echo  - %atlas_height%
PAUSE
endlocal