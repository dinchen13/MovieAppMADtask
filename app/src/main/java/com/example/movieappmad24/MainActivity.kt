package com.example.movieappmad24

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.movieappmad24.models.Movie
import com.example.movieappmad24.models.getMovies
import com.example.movieappmad24.ui.theme.MovieAppMAD24Theme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)     //das braucht man für die topbar, macht der typ im youtubevideo auch (von den bereitgestellten resourcen)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieAppMAD24Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val scrollbehavior = TopAppBarDefaults.pinnedScrollBehavior() //das ist wie sich die top bar beim scrollen verhalten soll zb (enterAlwaysScrollBehavior()) lässt die bar beim runterswipen verschwinden
                    Scaffold(
                        modifier= Modifier
                            .fillMaxSize()
                            .nestedScroll(scrollbehavior.nestedScrollConnection),  //optional
                        topBar = {
                            CenterAlignedTopAppBar(title = {
                                Text("Movie App")
                            },
                                scrollBehavior = scrollbehavior, //optional
                                colors= TopAppBarDefaults.topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.onBackground,  //hab das hinzugefügt zu Theme.kt damit ich dafür auch eine dynamische farbe hab
                                    titleContentColor = MaterialTheme.colorScheme.primary,
                                )
                            )
                        },
                        bottomBar = {
                            BottomAppBar(
                                containerColor = MaterialTheme.colorScheme.background,
                                contentColor = MaterialTheme.colorScheme.primary,
                            ) {
                                var watchlistSelected by remember { mutableStateOf(false) }
                                NavigationBarItem( selected = !watchlistSelected,
                                    onClick = {watchlistSelected=!watchlistSelected},
                                    label = {Text(text = "Home")},
                                    icon = {Icon(
                                        imageVector = Icons.Default.Home,
                                        contentDescription = "Home"
                                    )}
                                )
                                NavigationBarItem( selected = watchlistSelected,
                                    onClick = {watchlistSelected=!watchlistSelected},
                                    label = {Text(text = "Watchlist")},
                                    icon = {Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Watchlist"
                                    )}
                                )
                            }
                        },


                        ) {
                            innerPadding ->
                        Column(
                            modifier = Modifier
                                .padding(innerPadding),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            MovieList(movies = getMovies())
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MovieList(movies: List<Movie> = getMovies()){
    LazyColumn {
        items(movies) { movie ->
            MovieRow(movie)
        }
    }
}


@Composable
fun MovieRow(movie: Movie){
    var showDetails by remember {
        mutableStateOf(false)
    }

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp),
        shape = ShapeDefaults.Large,
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = movie.images[1],
                    contentDescription = null,
                    placeholder = painterResource(id = R.drawable.movie_image),
                    contentScale = ContentScale.Crop
                )
/*
                Image(
                    painter = painterResource(id = R.drawable.movie_image),
                    contentScale = ContentScale.Crop,
                    contentDescription = "placeholder image")
 */
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    contentAlignment = Alignment.TopEnd
                ){
                    Icon(
                        tint = MaterialTheme.colorScheme.secondary,
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Add to favorites")
                }
            }
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = movie.title)
                Icon(modifier = Modifier
                    .clickable {
                       showDetails = !showDetails
                    },
                    imageVector =
                        if (showDetails) Icons.Filled.KeyboardArrowDown
                        else Icons.Default.KeyboardArrowUp, contentDescription = "show more")
            }
            //Text(text = movie.plot, maxLines = if (showDetails) Int.MAX_VALUE else 1)   //das wär eine zweite option statt andimatedvisability dazu braucht man aber noch irgendwo ein .animateContentSize() beim Modifier
            AnimatedVisibility(showDetails) {
                Column(Modifier.padding(15.dp)) {
                    Text(text = "Director: " + movie.director)
                    Text(text = "Released: " + movie.year)
                    Text(text = "Genre: " + movie.genre)
                    Text(text = "Actors: " + movie.actors)
                    Text(text = "Rating: " + movie.rating)
                    Divider()
                    Text(text = "Plot: " + movie.plot)
                }
            }
        }
    }
}

@Preview
@Composable
fun DefaultPreview(){
    MovieAppMAD24Theme {
       MovieList(movies = getMovies())
    }
}
