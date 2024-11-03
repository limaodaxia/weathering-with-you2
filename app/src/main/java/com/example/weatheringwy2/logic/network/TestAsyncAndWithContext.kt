package com.example.weatheringwy2.logic.network

import kotlinx.coroutines.*

fun main() {
    runBlocking {
        val start = System.currentTimeMillis()
        val deferred1 =async {
            delay(1000)
            5+5
        }
        val deferred2 =async {
            delay(1000)
            4+6
        }
        println("ans = ${deferred1.await()+deferred2.await()}")
       /* val result = withContext(Dispatchers.Default){
            delay(1000)
            var sum = 5+5
            delay(1000)
            sum += 4 + 6
            sum
        }
        println(result)*/
        val end= System.currentTimeMillis()
        println(end-start)

    }
}
suspend fun test1(){

}

suspend fun test2(){

}