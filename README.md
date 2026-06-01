# 🎲 Azar — Generador aleatorio y tomador de decisiones

App de Android **100% autónoma** (sin servidores, sin base de datos, sin costes
de mantenimiento) pensada para **ganar dinero con anuncios poco invasivos**.

Reúne 7 herramientas que la gente usa de verdad en el día a día:

| Herramienta | Para qué sirve |
|-------------|----------------|
| 🪙 Cara o Cruz | Lanzar una moneda |
| 🎲 Dados | Tirar de 1 a 10 dados (d4, d6, d8, d10, d12, d20) |
| 🔢 Número aleatorio | Elegir un número en un rango |
| 👍 Sí o No | Que decida el azar |
| 🏆 Sorteo | Elegir un ganador de una lista de nombres |
| 🔑 Contraseña | Generar contraseñas seguras |
| 🎟️ Lotería | Números de la suerte sin repetir |

La app está en **español e inglés** (se adapta al idioma del móvil) y usa
**Material 3** con tema claro/oscuro automático.

---

## 💰 ¿Cómo gana dinero? (anuncios poco invasivos)

Usa **Google AdMob**, la red de anuncios oficial de Google. Está configurada
para **respetar al usuario**, que es lo que hace que la gente no desinstale la app:

1. **Banner inferior**: un anuncio pequeño fijo abajo. Es el formato menos molesto.
2. **Anuncio a pantalla completa (interstitial)**: aparece **solo de vez en cuando**,
   y únicamente **al salir de una herramienta**. Tiene dos límites de seguridad:
   - como mucho **1 cada 3 minutos**,
   - y solo tras **12 acciones** (tiradas/generaciones).

Nunca aparecen anuncios mientras el usuario está generando algo. Esto mantiene
buenas valoraciones en Google Play y, por tanto, más descargas e ingresos.

> Ahora mismo la app muestra **anuncios de PRUEBA de Google** (no generan dinero,
> pero te dejan ver que todo funciona). Para cobrar de verdad, sigue los pasos de
> abajo para poner tus propios identificadores de AdMob.

---

## 🚀 Cómo poner la app en marcha (sin saber programar)

### Paso 1 — Instala Android Studio
Descárgalo gratis desde https://developer.android.com/studio e instálalo.
La primera vez te pedirá descargar el "Android SDK": acepta todo.

### Paso 2 — Abre el proyecto
1. Abre Android Studio → **Open** (Abrir).
2. Selecciona la carpeta de este proyecto.
3. Espera a que termine de descargar dependencias (barra inferior). Necesita internet.

### Paso 3 — Pruébala
1. Conecta un móvil Android (con "Depuración USB" activada) o crea un emulador.
2. Pulsa el botón verde ▶ **Run**.
3. ¡Verás la app funcionando con anuncios de prueba!

### Paso 4 — Pon TUS anuncios para cobrar
1. Crea una cuenta gratis en **https://admob.google.com**.
2. Registra una app nueva y crea **dos bloques de anuncios**: un **Banner** y un
   **Interstitial**. Cada uno te da un ID con forma `ca-app-pub-XXXX/YYYY`.
3. Abre el archivo
   [`app/src/main/java/com/azar/decide/ads/AdConfig.kt`](app/src/main/java/com/azar/decide/ads/AdConfig.kt)
   y sustituye los valores de `PROD_BANNER` y `PROD_INTERSTITIAL` por los tuyos.
4. Abre [`app/src/main/AndroidManifest.xml`](app/src/main/AndroidManifest.xml) y
   sustituye el valor de `com.google.android.gms.ads.APPLICATION_ID` por **tu App ID** de AdMob.

> ⚠️ Importante: **nunca pulses tus propios anuncios reales**. Google podría
> banear tu cuenta. Por eso, mientras desarrollas (modo *debug*), la app siempre
> usa los anuncios de prueba automáticamente.

### Paso 5 — Publica en Google Play
1. Paga la cuota única de desarrollador de **Google Play** (~25 USD una sola vez):
   https://play.google.com/console
2. En Android Studio: **Build → Generate Signed App Bundle** y sigue el asistente
   para crear tu *keystore* (guárdalo bien, lo necesitarás siempre).
3. Sube el archivo `.aab` a Google Play Console, rellena la ficha de la tienda
   (descripción, capturas, icono) y envíala a revisión.
4. Para cumplir la normativa, configura también el **consentimiento de privacidad**
   (GDPR/UMP) en AdMob y la **política de privacidad** en Play Console.

---

## 🛠️ Detalles técnicos

- **Lenguaje:** Kotlin
- **UI:** Jetpack Compose + Material 3 (color dinámico en Android 12+)
- **Arquitectura:** una sola Activity + Navigation Compose
- **Ajustes:** Jetpack DataStore (tema y sonido)
- **Anuncios:** Google Mobile Ads SDK (AdMob)
- **minSdk:** 26 (Android 8.0) · **targetSdk/compileSdk:** 35
- **Sin backend:** todo el azar se calcula en el dispositivo. Cero costes de servidor.

### Estructura del proyecto
```
app/src/main/java/com/azar/decide/
├── AzarApplication.kt        # Inicializa AdMob
├── MainActivity.kt           # Punto de entrada + tema
├── ads/                      # Banner, interstitial y configuración de IDs
├── data/                     # Preferencias (DataStore)
├── feature/                  # Una carpeta por herramienta + inicio + ajustes
└── ui/                       # Tema, componentes y navegación
```

---

## 📈 Consejos para que la use mucha gente

- Pide a tus amigos una **valoración de 5 estrellas** al principio: sube el ranking.
- Una **ficha clara** en Play (buen icono, capturas, descripción con palabras como
  "sorteo", "moneda", "dados", "decisión", "aleatorio") trae descargas gratis.
- Mantén los anuncios suaves: una buena nota (4★+) es lo que más dinero da a largo plazo.

> Hecho con cariño. Que el azar esté de tu lado 🍀
