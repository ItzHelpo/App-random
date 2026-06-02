# 📦 Repón — Recordatorio de recompra

App de Android **100% offline y sin mantenimiento** que te avisa **antes** de que
se te acaben tus consumibles del día a día (café, pasta de dientes, pienso,
detergente, lentillas, pastillas…). Lo mejor: **aprende tu ritmo real** — cada vez
que marcas "Compré más", ajusta sola cuántos días te dura cada cosa.

## ✨ Qué hace
- Añades un producto, su icono y cada cuántos días se te acaba (aprox.).
- Repón calcula cuándo lo vas a necesitar y te lo ordena por urgencia.
- Te **notifica** unos días antes de que se acabe (tú eliges cuántos).
- Pulsas **"Compré más"** y empieza un ciclo nuevo… y la estimación se afina.
- Todo en tu móvil: **sin cuentas, sin servidores, sin conexión**.

## 💰 Monetización poco invasiva
- **Banner pequeño** en la pantalla principal (formato menos molesto).
- Sin anuncios a pantalla completa que interrumpan.
- Bilingüe: **español** e inglés (según el idioma del móvil).

> Trae los **ad units de PRUEBA de Google**, así funciona nada más compilar.
> Antes de publicar, pon tus IDs reales en
> [`AdConfig.kt`](app/src/main/java/com/repon/app/ads/AdConfig.kt) y el
> `APPLICATION_ID` en [`AndroidManifest.xml`](app/src/main/AndroidManifest.xml).

## 🛠️ Tecnología
- **Kotlin + Jetpack Compose + Material 3** (tema claro/oscuro y color dinámico).
- **Room** para la base de datos local.
- **WorkManager + notificaciones** para los avisos diarios.
- Google Mobile Ads (AdMob) · minSdk 26 · target/compileSdk 35.

## 📲 Probarla sin PC
Cada cambio compila el APK en la nube (GitHub Actions) y lo publica en
**[Releases](../../releases/tag/prueba)** como `Repon-prueba.apk`, listo para
descargar e instalar en el móvil.

## Estructura
```
app/src/main/java/com/repon/app/
├── ReponApp.kt          # Inicializa avisos + AdMob
├── MainActivity.kt      # Navegación y tema
├── data/                # Room: items, base de datos, cálculo y repositorio
├── notify/              # Notificaciones + WorkManager
├── ads/                 # Banner + configuración de IDs
└── ui/                  # Inicio, añadir/editar, ajustes, tema
```
