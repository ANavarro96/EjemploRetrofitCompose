package com.example.ejemploretrofitcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.ejemploretrofitcompose.model.Amiibo
import com.example.ejemploretrofitcompose.shared.ViewModelRetrofit
import com.example.ejemploretrofitcompose.shared.estadoApi
import com.example.ejemploretrofitcompose.ui.theme.EjemploRetrofitComposeTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EjemploRetrofitComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel : ViewModelRetrofit = viewModel()
                    val estado by viewModel.estadoLlamada.collectAsState()
                    val listaAmiibos by viewModel.listaAmiibos.collectAsState()
                    val listaGS by viewModel.listaGS.collectAsState()
                    val textoBusqueda by viewModel.textoBusqueda.collectAsState()
                    val activo by viewModel.activo.collectAsState()

                    if(estado == estadoApi.LOADING){
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(modifier = Modifier.padding(bottom = 16.dp))
                            Text(
                                text = "Cargando amibos!",
                                fontSize = 18.sp
                            )
                        }
                    }
                    else{
                        Column() {
                            SearchBar(
                                query = textoBusqueda,
                                onQueryChange = { viewModel.actualizarTextoBusqueda(it)
                                                },
                                onSearch = {
                                            viewModel.actualizarActivo(false)
                                            viewModel.buscarAmiibos()
                                           },
                                active = activo,
                                onActiveChange = {
                                    viewModel.actualizarActivo(it)
                                },
                                placeholder = { Text("Selecciona una serie de amiibos!") },
                                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                                trailingIcon = { Icon(Icons.Default.MoreVert, contentDescription = null)},
                                modifier = Modifier.fillMaxWidth(),
                                content = {
                                    Column(Modifier.verticalScroll(rememberScrollState())) {
                                        listaGS!!.amiibo?.distinctBy { it.name }!!
                                            .let { amiibos ->
                                                if (textoBusqueda.isNotEmpty() && textoBusqueda.isNotBlank()) {
                                                    amiibos.filter { it.name!!.startsWith(textoBusqueda,
                                                        true) }
                                                } else amiibos
                                            }.sortedBy { it.name!! }.forEach {
                                            ListItem(
                                                headlineContent = { it.name?.let { it1 -> Text(it1) } },
                                                modifier = Modifier
                                                    .clickable {
                                                        viewModel.actualizarTextoBusqueda(it.name!!)
                                                        viewModel.actualizarActivo(false)
                                                        viewModel.buscarAmiibos()
                                                    }
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 16.dp, vertical = 4.dp)
                                            )
                                        }
                                    }

                                }
                            )
                            LazyColumn(){
                                items(listaAmiibos!!.amiibo!!){
                                    ItemAmiibo(a = it)
                                }
                            }
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun ItemAmiibo(a : Amiibo){
    Card(elevation = CardDefaults.elevatedCardElevation(defaultElevation = 5.dp)){
        Row(Modifier.fillMaxWidth()) {
            var imageSize by remember { mutableStateOf(Size.Zero) }
            AsyncImage(model = a.image, contentDescription = null, modifier =
            Modifier
                .size(200.dp)
                .wrapContentWidth(if(imageSize.width < 400) Alignment.CenterHorizontally else
                Alignment.Start)
                .onSizeChanged { imageSize = it.toSize() }, contentScale = ContentScale.Fit,
                alignment = Alignment.Center)
            println("Image size: ${imageSize.width} x ${imageSize.height}")
            a.name?.let { Text(text = it, modifier = Modifier.fillMaxWidth().align(Alignment.CenterVertically)
                .wrapContentWidth(Alignment.CenterHorizontally, true),
                textAlign = TextAlign.Center) }
        }
    }

}
