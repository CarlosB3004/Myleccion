package com.example.myleccion

import android.os.Bundle
import androidx.compose.ui.tooling.preview.Preview
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myleccion.ui.theme.MyleccionTheme

data class ItemData(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val precio: String,
    val imagenRes: Int
)

// 3 pantallas
enum class Pantalla {
    INICIO,
    GRID,
    DETALLE
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyleccionTheme {
                AppMainScreen()
            }
        }
    }
}

@Composable
fun AppMainScreen() {
    // Estado para saber en qué pantalla estamos
    var pantallaActual by remember { mutableStateOf(Pantalla.INICIO) }
    // Estado para guardar el zapato que el usuario tocó
    var itemSeleccionado by remember { mutableStateOf<ItemData?>(null) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when (pantallaActual) {
            Pantalla.INICIO -> PantallaInicio(
                onNavegarGrid = { pantallaActual = Pantalla.GRID }
            )
            Pantalla.GRID -> PantallaGrid(
                onItemClick = { item ->
                    itemSeleccionado = item
                    pantallaActual = Pantalla.DETALLE
                },
                onVolver = { pantallaActual = Pantalla.INICIO }
            )
            Pantalla.DETALLE -> PantallaDetalle(
                item = itemSeleccionado,
                onVolver = { pantallaActual = Pantalla.GRID }
            )
        }
    }
}

@Composable
fun PantallaInicio(onNavegarGrid: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.logo_zapatos),
            contentDescription = "Logo",
            modifier = Modifier
                .size(180.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Bienvenido al Catalogo",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Explora y descubre detalles sobre multiples categorias",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onNavegarGrid) {
            Text("Ver Catálogo")
        }
    }
}

//  PANTALLA DEL CATÁLOGO
@Composable
fun PantallaGrid(
    onItemClick: (ItemData) -> Unit,
    onVolver: () -> Unit
) {
    // Lista de zapatos
    val listaItems = remember {
        listOf(
            ItemData(1, "Air Runner", "Zapatilla deportiva ligera", "$89.99", R.drawable.zapato1),
            ItemData(2, "Classic Leather", "Zapato formal de cuero", "$129.00", R.drawable.zapato2),
            ItemData(3, "Urban Sandal", "Sandalia cómoda y moderna", "$49.50", R.drawable.zapato3),
            ItemData(4, "Mountain Boot", "Bota para trekking", "$149.90", R.drawable.zapato4),
            ItemData(5, "Speed Racer", "Zapatilla para entrenamiento", "$99.00", R.drawable.zapato5),
            ItemData(6, "Elegant Heel", "Tacón elegante", "$79.99", R.drawable.zapato6)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Título
        Text(
            text = "Catálogo de Zapatos",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Cuadrícula de 2 columnas
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(listaItems) { item ->
                CardItemGrid(
                    item = item,
                    onClick = { onItemClick(item) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = onVolver) {
            Text("Atrás")
        }
    }
}

// TARJETA DE CADA ZAPATO
@Composable
fun CardItemGrid(item: ItemData, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = item.imagenRes),
                contentDescription = item.titulo,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.titulo,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = item.precio,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

// PANTALLA DE DETALLE
@Composable
fun PantallaDetalle(
    item: ItemData?,
    onVolver: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Imagen grande
                Image(
                    painter = painterResource(id = item?.imagenRes ?: R.drawable.logo_zapatos),
                    contentDescription = item?.titulo,
                    modifier = Modifier
                        .size(180.dp)
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Título
                Text(
                    text = item?.titulo ?: "Producto",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Precio
                Text(
                    text = item?.precio ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Descripción
                Text(
                    text = item?.descripcion ?: "",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onVolver) {
            Text("Atrás")
        }
    }
}

// PREVIEWS
@Preview(showBackground = true)
@Composable
fun PreviewPantallaInicio() {
    MyleccionTheme {
        PantallaInicio(onNavegarGrid = {})
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPantallaGrid() {
    MyleccionTheme {
        PantallaGrid(
            onItemClick = {},
            onVolver = {}
        )
    }
}

