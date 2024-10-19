package com.example.myquizgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay



class MainActivity : ComponentActivity() {
    // Main entry point of the app
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Use Jetpack Compose
        setContent {
            QuizApp() //  QuizApp composable function
        }
    }
}

@Composable
fun QuizApp() {
    //  handling navigation between screens
    val navController = rememberNavController()

    // Define the navigation host w
    NavHost(navController = navController, startDestination = "splash") {
        // splash screen route
        composable("splash") { SplashScreen(navController) }
        // quiz screen route
        composable("quiz") { QuizScreen(navController) }
        // score as an argument
        composable("Finalstats/{score}") { backStackEntry ->
            val score = backStackEntry.arguments?.getString("score")?.toInt() ?: 0
            StatsScreen(score)
        }
    }
}

@Composable
fun SplashScreen(navController: NavHostController) {
    // LaunchedEffect ensures the splash screen shows for 3 seconds before navigating
    LaunchedEffect(true) {
        delay(5000) //  5 seconds delay
        //  navigate to the quiz screen
        navController.navigate("quiz") {
            // Pop the splash screen from the backstack so the user can't return to it
            popUpTo("splash") { inclusive = true }
        }
    }

    //A simple text centered on the screen
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Pakistani Quiz Game", fontSize = 34.sp) // Displaying "Pakistani Quiz Game" text
    }
}

@Composable
fun QuizScreen(navController: NavHostController) {
    //  current question index
    var questionIndex by remember { mutableIntStateOf(0) }

    // Questions and the options of answers (with the correct answer in it too).
    val questions = listOf(
        Triple("What is the capital of Pakistan?", listOf("Lahore", "Islamabad", "Karachi"), "Islamabad"),
        Triple("How many provinces are there in Pakistan?", listOf("10", "7", "5"), "5"),
        Triple("Who wrote Pakistan's National Anthem?", listOf("Allama Iqbal", "Hafeez Jalandhari", "Quaid-e-Azam"), "Hafeez Jalandhari"),
        Triple("What is the national language of Pakistan?", listOf("Urdu", "Punjabi", "Pashto"), "Urdu"),
        Triple("Who was the Founder of Pakistan", listOf("Quaid-e-Azam", "Mirza Ghalib", "Sir Syed Ahmed Khan"), "Quaid-e-Azam"),
        Triple("What is the national sport of Pakistan", listOf("Cricket", "Football", "Hockey"), "Hockey"),
        Triple("Which is the highest mountain peak in Pakistan?", listOf("Everest", "K2", "Shimla"), "K2")
    )

    //  selected answer and score
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var score by remember { mutableIntStateOf(0) }

    // question, answers, and the correct answer.
    val currentQuestion = questions[questionIndex]
    val questionText = currentQuestion.first
    val answers = currentQuestion.second
    val correctAnswer = currentQuestion.third

    //displaying the question and answers
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display the current question
        Text(text = questionText, fontSize = 20.sp, modifier = Modifier.padding(bottom = 16.dp))

        //  radio buttons for each possible answer
        answers.forEach { answer ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                //
                RadioButton(
                    selected = (selectedAnswer == answer),
                    onClick = { selectedAnswer = answer } // select answer when clicked
                )
                Text(text = answer, modifier = Modifier.padding(start = 8.dp)) // Display the answer text
            }
        }

        Spacer(modifier = Modifier.height(20.dp)) // Add space

        // Confirm button to check the answer and move to the next question
        Button(
            onClick = {
                if (selectedAnswer == correctAnswer) {
                    //  increment the score if the answer is correct
                    score += 1
                }

                if (questionIndex < questions.size - 1) {
                    // next question
                    questionIndex += 1
                    selectedAnswer = null // Reset
                } else {
                    // If it's the last question, navigate to the stats screen
                    navController.navigate("Finalstats/$score")
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally) // confirm button
        ) {
            Text("Confirm")
        }
    }
}

@Composable
fun StatsScreen(score: Int) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Display the quiz completion message and the final score
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Quiz has ended!", fontSize = 28.sp) // completion text
            Text(text = "Your score: $score", fontSize = 20.sp, modifier = Modifier.padding(top = 16.dp)) // Displaying the final score
        }
    }
}