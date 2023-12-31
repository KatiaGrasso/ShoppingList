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
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.vector.ImageVector
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
    var deleteAlert = remember {mutableStateOf(false) }

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
                                deleteAlert.value= true
                            }, Modifier.drawWithContent { drawContent()
                            }) {
                                Icon(
                                    Icons.Filled.Delete,
                                    contentDescription = "Clear all",
                                )
                                if(deleteAlert.value==true) {
                                    MyAlertDialog(
                                        onDismissRequest = {deleteAlert.value=false},
                                        onConfirmation = {
                                            viewModel.clearItems()
                                            deleteAlert.value=false
                                        },
                                        dialogTitle = "Attenzione",
                                        dialogText = "Sei sicuro di voler cancellare l'intera lista?",
                                        icon = Icons.Outlined.Warning
                                    )
                                }
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
                                val openAlertDialog = remember { mutableStateOf(false) }

                                if(openAlertDialog.value==true) {

                                    AlertDialog(
                                        icon = {Icon(Icons.Outlined.Warning, contentDescription = "Warning Icon") },
                                        title = {Text(text = "Attenzione")},
                                        text = {Text(
                                            textAlign = TextAlign.Center,
                                            text = "Non hai aggiunto la categoria o la descrizione. Se non lo fai, non verrà aggiunta nessuna voce alla lista.")},
                                        onDismissRequest = {openAlertDialog.value=false},
                                        confirmButton = {
                                            TextButton(
                                                onClick = {
                                                    openAlertDialog.value=false
                                                }
                                            ) {
                                                Text("Chiudi")
                                            }
                                        }
                                    )

                                   /* MyAlertDialog(
                                        onDismissRequest = {
                                            openAlertDialog.value=false
                                        },
                                        onConfirmation = {
                                            openAlertDialog.value=false
                                            showDialog=false
                                        },
                                        dialogTitle = "Attenzione",
                                        dialogText = "Non hai aggiunto la categoria o la descrizione. Se confermi, non verrà aggiunta nessuna voce alla lista.",
                                        icon = Icons.Outlined.Warning
                                    ) */
                                }

                                PopupMenu(viewModel) //contenuto pop-up: richiamo l'analoga funzione in PurchaseViewModel
                                Button(
                                    onClick = {
                                        if(description_toAdd.equals("") || category_toAdd.equals("") || category_toAdd.equals("Categoria")){
                                            openAlertDialog.value=true
                                        } else {
                                            viewModel.addItem(description_toAdd, category_toAdd)
                                            showDialog = false;
                                        }
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
                                var aggiungiCategoria by remember{mutableStateOf(false)}
                                var modificaCategoria by remember{mutableStateOf(false)}
                                var categoriaDaModificare by remember{mutableStateOf("")}
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
                                                        viewModel.removeCategory(item)
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
                                                                modificaCategoria=true
                                                                categoriaDaModificare=item
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
                                Column(modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Box(modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp)
                                        .padding(bottom = 8.dp),
                                        contentAlignment = Alignment.Center,

                                        ){
                                        Button(onClick = { aggiungiCategoria=true }) {
                                            Text(text = "Aggiungi categoria")
                                    }}
                                }

                                if(aggiungiCategoria){
                                    AddCategory(viewModel)
                                    Row(modifier=Modifier.padding(top=8.dp))
                                    {
                                        Button(onClick = {
                                            aggiungiCategoria = false
                                            if (viewModel.addCategory(category_toAdd)) {
                                                //pop up che la categoria già esiste
                                            }},
                                            modifier=Modifier.padding(start=4.dp)
                                            )
                                        {
                                            Text(text = "Conferma")
                                        }
                                        Button(onClick = { aggiungiCategoria = false },
                                            modifier=Modifier.padding(start=8.dp),
                                            ) {
                                            Text(text = "Annulla")
                                        }
                                    }

                                }
                                if(modificaCategoria){
                                    ModifyCategory(viewModel = viewModel, cat = categoriaDaModificare)
                                    Row(){
                                        Button(onClick = {
                                            modificaCategoria = false
                                            viewModel.modifyCategory(categoriaDaModificare, category_toAdd)
                                            viewModel.modifyKeys(categoriaDaModificare, category_toAdd)
                                            })
                                        {
                                            Text(text = "Conferma")
                                        }
                                        Button(onClick = {modificaCategoria = false }) {
                                            Text(text = "Annulla")
                                        }
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
                        val openAlertDialog = remember { mutableStateOf(false) }
                        val deleteCat = SwipeAction(
                            onSwipe = {
                                openAlertDialog.value = true
                                      },
                            icon = rememberVectorPainter(deleteIcon),
                            background = Color.Red
                        )

                        if(openAlertDialog.value==true) {
                            MyAlertDialog(
                                onDismissRequest = {openAlertDialog.value=false},
                                onConfirmation = {
                                    viewModel.removeCategoryAndItems(category)
                                    openAlertDialog.value=false
                                                 },
                                dialogTitle = "Attenzione",
                                dialogText = "Sei sicuro di voler eliminare la categoria? Se confermi, tutti prodotti di questa categoria verranno eliminati.",
                                icon = Icons.Outlined.Warning
                            )
                        }

                        SwipeableActionsBox(endActions = listOf(deleteCat)) {
                            Card(
                                onClick = { visible = !visible },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp)
                                    .size(50.dp),
                                shape = RoundedCornerShape(10.dp)
                                ) {
                                Box(
                                    modifier = Modifier.fillMaxWidth()
                                ) {

                                    Text(
                                        text = item.key,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(8.dp),
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
                                                list.forEachIndexed{index, x->viewModel.checkItem(x, index)
                                                visible=!visible
                                                visible=!visible //queste due righe visivamente non fanno niente, ma forzano l'aggiornamento della vista
                                                 }
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
                        }



                        list.forEachIndexed { index, itemOfList ->
                            var isChecked = itemOfList.isPurchased

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
                                                viewModel.checkItem(itemOfList, index)
                                                visible=!visible
                                                visible=!visible
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
                
                //Text(text = mappaVista.toString())
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(
                textAlign = TextAlign.Center,
                text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Conferma")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Annulla")
            }
        }
    )
}


