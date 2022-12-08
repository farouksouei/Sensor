package com.example.myapplication

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.getSystemService
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Scaffold {
                        MainAPP()
                    }
                }
            }
        }
    }
}

@Composable
fun MainAPP(){
    val temperature = remember { mutableStateOf("0") }
    Column {
        TempSensor(temperature)
        ShowTemp(temperature)
    }
}
/*
@Composable
fun Sensors() {
    val context = LocalContext.current
    val sensorManager = context.getSystemService<SensorManager>()
    val deviceSensors= sensorManager?.getSensorList(Sensor.TYPE_ALL) as List<Sensor>
    LazyColumn(content ={
        items(deviceSensors.size)
        {
            Text(deviceSensors[it].name)
            Text(deviceSensors[it].stringType)
        }
    })
}
*/

//Temperature sensor
@Composable
fun TempSensor(temperature: MutableState<String>) {
    val context = LocalContext.current
    val sensorManager = context.getSystemService<SensorManager>()
    val tempSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
    val tempListener = object : SensorEventListener {
         fun onStart() {
            sensorManager?.registerListener(this, tempSensor, 20000)
        }
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            Log.d("Sensor", "Accuracy changed")
        }

        override fun onSensorChanged(event: SensorEvent?) {
            Log.d("Sensor", "Sensor changed")
            if (event != null) {
                Log.d("Sensor", "Sensor changed: ${event.values[0]}")
                temperature.value = event.values[0].toString()
            }
        }
        fun onStop() {
            sensorManager?.unregisterListener(this)
        }
    }
    Column (modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Temperature: ${tempSensor?.name}", style = MaterialTheme.typography.h5)
        Text("Type: ${tempSensor?.stringType}", style = MaterialTheme.typography.h6)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(onClick = { tempListener.onStart() }) {
                Text("Start")
            }
            Button(onClick = { tempListener.onStop() }) {
                Text("Stop")
        }

        }
    }
}

@Composable
fun ShowTemp(temperature: MutableState<String>) {
    Text(text = "temperature is ${temperature.value}", style = MaterialTheme.typography.h2 )
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}