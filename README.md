# ColorsApp: Paleta de colores
 
Aplicación Android (Kotlin) que muestra 3 CardViews horizontales y un RecyclerView con 5 CardViews verticales superpuestos. Cada CardView tiene una opacidad fija, y el usuario puede cambiar su color desde un Dialog.
 
## 1. Descripción
 
La pantalla principal contiene:
 
- Un título superior ("Paleta de colores").
- 3 CardViews horizontales (**H1 20%**, **H2 50%**, **H3 80%**) con su texto alineado arriba a la izquierda.
- Un RecyclerView horizontal con 5 CardViews verticales (**V1 20%**, **V2 35%**, **V3 50%**, **V4 65%**, **V5 80%**), superpuesto a las filas.
- Un CardView que hace de botón **"Cambiar color"**.
Al pulsar el botón se abre un Dialog con los 8 CardViews y los 9 colores disponibles. Al pulsar **"Aplicar color"**, el CardView elegido cambia al color elegido manteniendo su opacidad.
 
## 2. Estructura de archivos
 
| Archivo | Función |
|---|---|
| `ColorsActivity.kt` | Activity principal: inicializa las views, gestiona el Dialog y repinta los colores |
| `ColorCard.kt` | Data class con nombre, opacidad y color de cada CardView |
| `PaletteColor.kt` | Sealed class con los 9 colores y sus 5 opacidades |
| `BarAdapter.kt` | Adapter del RecyclerView |
| `BarViewHolder.kt` | ViewHolder: dibuja cada barra vertical |
| `activity_colors.xml` | Layout principal |
| `item_bar.xml` | Plantilla de cada barra vertical |
| `dialog_color.xml` | Layout del Dialog con dos columnas de RadioButtons |
| `colors.xml` | Paleta: 9 colores x 5 opacidades = 45 etiquetas |
 
## 3. Paleta de colores y opacidad
 
Los colores se definen en `colors.xml` en formato **ARGB** (`#AARRGGBB`), donde los dos primeros dígitos hexadecimales son el canal alpha (opacidad). El alpha va de 0 a 255, así que cada porcentaje se calcula como `porcentaje x 255 / 100`:
 
| Opacidad | Cálculo | Hex |
|---|---|---|
| 20% | 51 | `33` |
| 35% | 89 | `59` |
| 50% | 128 | `80` |
| 65% | 166 | `A6` |
| 80% | 204 | `CC` |
 
Hay 9 colores (blanco, rojo, naranja, amarillo, verde, cian, azul, violeta y negro) y 5 opacidades, lo que da **45 etiquetas** con el patrón `color_opacidad`:
 
```xml
<color name="blue_20">#332196F3</color>
<color name="blue_35">#592196F3</color>
<color name="blue_50">#802196F3</color>
<color name="blue_65">#A62196F3</color>
<color name="blue_80">#CC2196F3</color>
```
 
Cuando dos colores se solapan, Android los mezcla así:
 
```
resultado = alpha x color_de_encima + (1 - alpha) x color_de_debajo
```
 
Por eso H3 (80%) deja pasar solo un 20% de lo que hay debajo, mientras que H1 (20%) deja ver casi todo.
 
## 4. Superposición de las vistas
 
Las filas y el RecyclerView están dentro de un `FrameLayout`, que apila a sus hijos en el mismo espacio. **El orden en el XML es el orden de dibujado**: lo que se escribe primero queda debajo y lo que se escribe después queda encima.
 
En esta app el RecyclerView se declara primero y las 3 filas después, de modo que las filas quedan delante, como se aprecia en la imagen del enunciado (H3 al 80% tapa casi por completo las barras).
 
Detalles importantes:
 
- Se usa `cardElevation="0dp"` en los CardViews para evitar sombras que ensuciarían la transparencia.
- Los CardViews no son clicables, así que los toques atraviesan las filas y llegan al RecyclerView, que sigue desplazándose.
## 5. Scroll horizontal
 
Cada barra mide 80dp más 24dp de márgenes (104dp). Las cinco barras suman unos 520dp, más de lo que cabe en la mayoría de pantallas, así que el `LinearLayoutManager` en horizontal permite desplazarlas sobre las filas H1, H2 y H3.
 
## 6. Diseño del código
 
### `ColorCard`: la opacidad es fija, el color cambia
 
```kotlin
data class ColorCard(val name: String, val opacity: Int, var color: PaletteColor)
```
 
`opacity` es un `val` (no cambia nunca) y `color` es un `var` (lo modifica el usuario). Así, cambiar el color nunca altera la opacidad de ese CardView.
 
### `PaletteColor`: conjunto cerrado de colores
 
Es una `sealed class` con un `object` por color. Cada uno guarda sus 5 recursos de `colors.xml`, y el método `getRes(opacity)` devuelve el que corresponde:
 
```kotlin
fun getRes(opacity: Int): Int {
    return when (opacity) {
        20 -> res20
        35 -> res35
        50 -> res50
        65 -> res65
        else -> res80
    }
}
```
 
Por ejemplo, `PaletteColor.Blue.getRes(35)` devuelve `R.color.blue_35`.
 
### Separación de datos y vista
 
La Activity cambia el dato y luego repinta. El Adapter y el ViewHolder solo dibujan lo que hay en los datos:
 
```kotlin
selectedCard.color = selectedColor
updateColors()
```
 
## 7. Funcionamiento del Dialog
 
1. El botón "Cambiar color" llama a `showDialog()`.
2. El Dialog tiene dos `RadioGroup` independientes: uno con los CardViews (H1-H3, V1-V5) y otro con los colores. Cada grupo garantiza que solo haya una opción marcada.
3. H1 y blanco vienen marcados por defecto en el XML, por lo que `checkedRadioButtonId` nunca devuelve `-1`.
4. Al pulsar "Aplicar color" se lee el id del RadioButton marcado en cada grupo:
```kotlin
val selectedCardId = rgRows.checkedRadioButtonId
val selectedColorId = rgColors.checkedRadioButtonId
```
 
5. Un `when` traduce cada id al `ColorCard` o al `PaletteColor` correspondiente.
6. Se asigna el nuevo color, se llama a `updateColors()` y se cierra el Dialog.
## 8. Repintado
 
`updateColors()` hace dos cosas:
 
- Repinta las 3 filas horizontales con `setCardBackgroundColor`.
- Llama a `notifyDataSetChanged()` para que el RecyclerView vuelva a dibujar las barras.
El RecyclerView reutiliza los ViewHolders al hacer scroll, por eso `render()` asigna siempre todos los datos de la barra (texto y color) en cada llamada.
 
## 9. Interpretaciones del enunciado
 
- **Qué va delante:** el enunciado dice que las barras "se superponen" a las filas, pero en la imagen de referencia H3 (80%) tapa las barras, por lo que se siguió la imagen. Para el comportamiento literal bastaría con intercambiar el orden de los dos bloques dentro del `FrameLayout`.
- **TextView superior:** el título es un TextView dentro de un CardView con fondo de color. Visualmente equivale al TextView superior pedido.
- **Variables de selección:** el RadioButton elegido de cada grupo se obtiene con `checkedRadioButtonId` y se traduce con un `when` al CardView y al color correspondientes.
## 10. Limitaciones
 
Los colores elegidos no se guardan. Si se rota la pantalla o se cierra la app, la Activity se recrea y los CardViews vuelven a sus colores iniciales. Para persistirlos se podría usar `onSaveInstanceState` o `SharedPreferences`.
