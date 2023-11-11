package com.example.shoppinglist
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import kotlinx.coroutines.launch
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

class PurchaseActivity : ComponentActivity() {

    val viewModel by viewModels<PurchaseViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.addItem("Carote", "Verdura")
        viewModel.addItem("Zucchine", "Verdura")
        viewModel.addItem("Uva", "Frutta")
        setContent {
            //TODO da inserire in un box sotto la voce descrizione
            MainScreen(viewModel)
        }
    }
}
@SuppressLint("MutableCollectionMutableState", "UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun MainScreen(viewModel: PurchaseViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    val mappaVista by viewModel.map.observeAsState()
    val categories by viewModel.categories.observeAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()


    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter

    ) {
        Row {
            Scaffold(
                bottomBar = {
                    BottomAppBar(
                        modifier = Modifier.zIndex(if (drawerState.isOpen) 1f else 0f),
                        //per vedere la bottom bar sopra quando apro il side menu
                        actions = {
                            IconButton(onClick = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            },) {
                                Icon(Icons.Filled.List, contentDescription = "All categories")
                            }

                            IconButton(onClick = {
                                viewModel.clearItems()
                            }, Modifier.drawWithContent { drawContent()
                            }) {
                                Icon(
                                    Icons.Filled.Delete,
                                    contentDescription = "Clear all",
                                )
                            }
                        },
                        floatingActionButton = {
                            FloatingActionButton(
                                onClick = {
                                    run { showDialog = true }
                                    Modifier.drawWithContent {
                                        drawContent()
                                    }
                                },
                                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                            ) {
                                Icon(Icons.Filled.Add, "Add")
                            }
                        }
                    )
                },
            ) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .zIndex(if (drawerState.isOpen) 1f else 0f)
                    //per vedere il side menu sopra
                ) {
                    ModalNavigationDrawer(
                        drawerContent = {
                            ModalDrawerSheet {
                                Text("Drawer title", modifier = Modifier.padding(16.dp))
                                Divider()
                                NavigationDrawerItem(
                                    label = { Text(text = "Drawer Item") },
                                    selected = false,
                                    onClick = { /*TODO*/ }
                                )
                                // ...other drawer items
                            }
                        }
                    ) {
                        // Screen content
                    }
                    if (showDialog) {
                        // Dialog per il popup
                        Dialog(
                            onDismissRequest = {
                                // Chiudi il popup quando l'utente tocca all'esterno
                                showDialog = false
                            }
                        ) {
                            // Contenuto del popup
                            Box(
                                modifier = Modifier //specifiche estetiche del pop up
                                    .width(600.dp)
                                    .height(300.dp)
                                    .padding(16.dp)
                                    .background(Color.White),
                                contentAlignment = Alignment.Center
                            ) {
                                PopupMenu(viewModel) //contenuto pop-up: richiamo l'analoga funzione in PurchaseViewModel
                                Button(
                                    onClick = {
                                        showDialog = false;
                                        viewModel.addItem(description_toAdd, category_toAdd)
                                    },
                                    modifier = Modifier.align(Alignment.BottomEnd)
                                ) {
                                    Text("Conferma")
                                }

                            }
                        }
                    }

                    ModalNavigationDrawer(
                        drawerState = drawerState,
                        drawerContent = {
                            ModalDrawerSheet {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(0.dp, 20.dp, 0.dp, 20.dp),
                                    text = "Categorie", fontSize = 25.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                               categories?.forEach { item ->
                                    // per ogni categoria ho una riga con nome e icona per editarlo
                                            Column(modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Box(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    contentAlignment = Alignment.CenterEnd
                                                )
                                                {
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(8.dp),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Text(
                                                        text = item,
                                                        fontSize = 18.sp,
                                                        modifier = Modifier.fillMaxWidth(),
                                                    )
                                                }

                                                    IconButton(onClick = {
                                                        viewModel.removeCategoryAndItems(item)
                                                    }, Modifier.drawWithContent {
                                                        drawContent()
                                                    }) {
                                                        Icon(
                                                            imageVector = Icons.Outlined.Delete,
                                                            contentDescription = "Delete",

                                                        )
                                                    }
                                                    Row(
                                                        modifier = Modifier
                                                            .align(Alignment.CenterEnd)
                                                            .padding(0.dp, 0.dp, 18.dp, 0.dp)
                                                    ) {
                                                        IconButton(
                                                            onClick = {

                                                            }
                                                        ) {
                                                            Icon(
                                                                imageVector = Icons.Outlined.Edit,
                                                                contentDescription = "Modifica"
                                                            )
                                                        }
                                                        Spacer(modifier = Modifier.width(8.dp))
                                                    }
                                                }
                                                Divider()
                                                Spacer(modifier = Modifier.width(8.dp))
                                            }

                                }




                            }
                        },
                    ) {
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,


                    ) {

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Lista della spesa", fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    mappaVista?.forEach { item ->
                        var visible by remember { mutableStateOf(false) } //flag che indica se gli item di una categoria sono visibili o meno
                        val density = LocalDensity.current
                        var list = item.value
                        var category = item.key
                        var arrowDown = Icons.Outlined.KeyboardArrowDown
                        var arrowUp = Icons.Outlined.KeyboardArrowUp
                        if (visible) {
                            arrowDown = arrowUp
                        }
                        val deleteIcon = Icons.Outlined.Delete

                        Card(
                            onClick = { visible = !visible },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp)
                                .size(50.dp),
                            shape = RoundedCornerShape(10.dp),
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                Text(
                                    text= item.key,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(8.dp)
                                )
                                Icon(
                                    imageVector = arrowDown,
                                    contentDescription = "Apri tendina",
                                    modifier = Modifier
                                        .size(30.dp)
                                        .align(Alignment.CenterEnd)
                                        .padding(0.dp, 0.dp, 10.dp, 0.dp)
                                )

                                Row(
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd)
                                        .padding(0.dp, 0.dp, 12.dp, 0.dp)
                                ) {
                                    IconButton(
                                        onClick = {
                                            //check tutte le voci della categoria
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "check",
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                }
                            }
                        }




                        list.forEachIndexed { index, itemOfList ->
                            var isChecked by remember { mutableStateOf(itemOfList.isPurchased) }
                            //Componente necessario per gestire la visibilità
                            AnimatedVisibility(
                                visible = visible,
                                enter = slideInVertically(animationSpec = tween(durationMillis = 700)) {
                                    with(density) { -40.dp.roundToPx() }
                                } + expandVertically(
                                    expandFrom = Alignment.Top,
                                    animationSpec = tween(durationMillis = 700)
                                ) + fadeIn(),
                                exit = shrinkVertically(animationSpec = tween(durationMillis = 700)) + fadeOut()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(6.dp)
                                ) {
                                    val delete = SwipeAction(
                                        onSwipe = { viewModel.removeItem(itemOfList) },
                                        icon = rememberVectorPainter(deleteIcon),
                                        background = Color.Red
                                    )
                                    SwipeableActionsBox(endActions = listOf(delete)) {
                                        ItemRow(
                                            item = itemOfList,
                                            isChecked = isChecked,
                                            onCheckedChange = {
                                                isChecked = it
                                                itemOfList.isPurchased = it
                                            },
                                            onDeleteClick = {
                                                viewModel.removeItem(itemOfList);

                                            }, mappa = mappaVista ?: emptyMap()
                                        )
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListItem(
    headlineContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    overlineContent: (@Composable () -> Unit)? = null,
    supportingContent: (@Composable () -> Unit)? = null,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    colors: ListItemColors = ListItemDefaults.colors(),
    tonalElevation: Dp = ListItemDefaults.Elevation,
    shadowElevation: Dp = ListItemDefaults.Elevation
) : Unit {

}

@Composable
    fun ItemRow(
        item: PurchasableItem,
        isChecked: Boolean,
        onCheckedChange: (Boolean) -> Unit,
        onDeleteClick: () -> Unit,
        mappa: Map<String, List<PurchasableItem>>
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.padding(2.dp)) {
                Text(
                    text = item.description,
                    fontSize = 18.sp
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Checkbox
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { onCheckedChange(it) }
                )

            }
        }

    }





