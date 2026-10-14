# Panduan Mengatasi Folder `.github` yang Tersembunyi di Android

Di handphone Android, folder yang diawali tanda titik (`.github`) otomatis disembunyikan oleh *File Manager* bawaan, sehingga saat Anda mengekstrak ZIP dan mengunggahnya ke GitHub lewat HP, folder `.github/workflows/build-apk.yml` sering tidak ikut terunggah.

## Cara Membuat Workflow Build APK Muncul di GitHub (Dari HP Android)

1. Setelah mengunggah file proyek ke repositori GitHub Anda, buka halaman utama repositori tersebut di browser.
2. Ketuk tombol **Add file** → pilih **Create new file**.
3. Pada kotak nama file (`Name your file...`), ketik persis seperti ini (termasuk tanda titik dan garis miringnya):
   `.github/workflows/build-apk.yml`
4. Salin (*copy*) seluruh isi file **`WORKFLOW_GITHUB_ACTIONS_COPY_INI/build-apk.yml`** (atau dari file `build-apk.yml` terpisah) lalu tempel (*paste*) ke kotak editor GitHub.
5. Ketuk tombol hijau **Commit changes...** di pojok kanan atas.
6. Sekarang buka tab **Actions** di GitHub Anda — workflow **Build DompetKu Android APK** akan langsung muncul dan berjalan otomatis!
