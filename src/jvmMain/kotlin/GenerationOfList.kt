import kotlin.math.sqrt

fun generation(n: Int) : MutableList<Float> {
    var temp: Double
    val list = mutableListOf<Float>()
    for (i in 0..n - 1) {
        temp = Math.random()
        if (temp <= 0.5) {
            list.add(sqrt(2 * temp).toFloat())
        } else {
            list.add(2 - sqrt(2 * (1 - temp)).toFloat())
        }
    }
    list.sort()
    return list
}

fun calculation(n: Int, list: List<Float>): List<Double> {
    var M = 0.0
    var D = 0.0
    var Mx2 = 0.0
    val p = 1 / n.toDouble()
    for (i in list) {
        M += i * p
        Mx2 += i * i * p
    }
    D = Mx2 - M * M
    return listOf(M, D)
}

fun separation(n: Int, list: List<Float>): List<List<Double>>{
    val listBar: MutableList<MutableList<Double>> =
        MutableList(4) {
            mutableListOf<Double>(); mutableListOf<Double>()
            mutableListOf<Double>(); mutableListOf<Double>()}

    val m =
        if(n == 50) 15
        else if(n == 100) 15
        else if(n == 1000) 25
        else 25
    var listSize = 0
    val deltaX = (list.last() - list.first()) / m
    var x = list.first().toDouble()
    var Nj = 0
    var index = 0
    var Fprev = 0.0
    while(x < list.last() && listSize != m){
        for(i in index..list.size - 1){
            if(list[i] < (x + deltaX) && list[i] >= x){
                Nj++
            } else {
                listBar[1].add(Nj.toDouble())
                index = i
                listBar[2].add(
                    if (list[i] <= 1) (((list[i] * list[i] / 2).toDouble() - Fprev) * n)
                    else ((-list[i] * list[i] / 2 + 2 * list[i] - 1).toDouble() - Fprev) * n
                )

                val teor = if (list[i] <= 1) (((list[i] * list[i] / 2).toDouble() - Fprev) * n)
                else ((-list[i] * list[i] / 2 + 2 * list[i] - 1).toDouble() - Fprev) * n
                val exp = Nj

                Fprev = if (list[i] <= 1) {
                    (list[i] * list[i] / 2).toDouble()
                } else {
                    (-list[i] * list[i] / 2 + 2 * list[i] - 1).toDouble()
                }
                listSize++
                Nj = 0
                break
            }
            if(list[i] == list.last() && Nj != 0){
                listBar[1].add(Nj.toDouble())
                listBar[2].add(if(list[i] <= 1) (((list[i] * list[i] / 2).toDouble() - Fprev) * n)
                                else ((-list[i] * list[i] / 2 + 2 * list[i] - 1).toDouble() - Fprev) * n)
                Fprev = if(list[i] <= 1) {(list[i] * list[i] / 2).toDouble()}
                        else {(-list[i] * list[i] / 2 + 2 * list[i] - 1).toDouble()}
                listSize++
                Nj = 0
            }
        }
        x += deltaX
        listBar[0].add(x)
    }

  //  var index = 0
   // var Nj = 0
  /*  for(i in list){
        if(i <= listBar[0][index]){
            Nj++
        } else {
            index++
            listBar[1].add(Nj.toDouble() / n.toDouble())
            Nj = 0
            val nf = Nj.toDouble() / n.toDouble()
        }
    }
    */
    listBar[3] = criterion(n, listBar[2], listBar[1])
    return listBar
}


fun criterion(n: Int, listTeor: List<Double>, listExper: List<Double>) : MutableList<Double> {
    val m = listTeor.size
    var x = 0.0
    for(j in 0..m - 1){
        if(listTeor[j] != 0.0) {
            x += (listExper[j] - listTeor[j]) * (listExper[j] - listTeor[j]) / listTeor[j]
        }
    }
    val r = m - 2 - 1
    val probability = when(r) {
        12 ->
            if(x >= 26.2) 1 //критерий не выполняется
            else if(x > 18.5 && x < 26.2) 2 //критерий не выполняется, недостаточный объем выборки
            else 3 //критерий выполняется
        22 ->
            if(x >= 40.3) 1 //критерий не выполняется
            else if(x > 30.8 && x < 40.3) 2 //критерий не выполняется, недостаточный объем выборки
            else 3 //критерий выполняется
        else -> 0
    }
    return mutableListOf(x, probability.toDouble())
}