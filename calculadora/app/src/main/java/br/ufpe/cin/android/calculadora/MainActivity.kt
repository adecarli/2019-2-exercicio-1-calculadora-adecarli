package br.ufpe.cin.android.calculadora

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var evaluated = false
    var invalid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Adiciona o dígito pressionado à string de exibição
        btn_0.setOnClickListener {
            // Se acabamos de avaliar uma expressão, limpe e coloque apenas o digito pressionado
            val str =   if (!evaluated)
                            text_calc.text.toString() + "0"
                        else {
                            evaluated = false
                            "0"
                        }
            text_calc.setText(str)
        }
        btn_1.setOnClickListener {
            val str =   if (!evaluated)
                            text_calc.text.toString() + "1"
                        else {
                            evaluated = false
                            "1"
                        }
            text_calc.setText(str)
        }
        btn_2.setOnClickListener {
            val str =   if (!evaluated)
                            text_calc.text.toString() + "2"
                        else {
                            evaluated = false
                            "2"
                        }
            text_calc.setText(str)
        }
        btn_3.setOnClickListener {
            val str =   if (!evaluated)
                            text_calc.text.toString() + "3"
                        else {
                            evaluated = false
                            "3"
                        }
            text_calc.setText(str)
        }
        btn_4.setOnClickListener {
            val str =   if (!evaluated)
                            text_calc.text.toString() + "4"
                        else {
                            evaluated = false
                            "4"
                        }
            text_calc.setText(str)
        }
        btn_5.setOnClickListener {
            val str =   if (!evaluated)
                            text_calc.text.toString() + "5"
                        else {
                            evaluated = false
                            "5"
                        }
            text_calc.setText(str)
        }
        btn_6.setOnClickListener {
            val str =   if (!evaluated)
                            text_calc.text.toString() + "6"
                        else {
                            evaluated = false
                            "6"
                        }
            text_calc.setText(str)
        }
        btn_7.setOnClickListener {
            val str =   if (!evaluated)
                            text_calc.text.toString() + "7"
                        else {
                            evaluated = false
                            "7"
                        }
            text_calc.setText(str)
        }
        btn_8.setOnClickListener {
            val str =   if (!evaluated)
                            text_calc.text.toString() + "8"
                        else {
                            evaluated = false
                            "8"
                        }
            text_calc.setText(str)
        }
        btn_9.setOnClickListener {
            val str =   if (!evaluated)
                            text_calc.text.toString() + "9"
                        else {
                            evaluated = false
                            "9"
                        }
            text_calc.setText(str)
        }
        btn_Dot.setOnClickListener {
            val str =   if (!evaluated)
                            text_calc.text.toString() + "."
                        else {
                            evaluated = false
                            "."
                        }
            text_calc.setText(str)
        }
        btn_LParen.setOnClickListener {
            val str =   if (!evaluated)
                            text_calc.text.toString() + "("
                        else {
                            evaluated = false
                            "("
                        }
            text_calc.setText(str)
        }
        btn_RParen.setOnClickListener {
            val str =   if (!evaluated)
                            text_calc.text.toString() + ")"
                        else {
                            evaluated = false
                            ")"
                        }
            text_calc.setText(str)
        }

        btn_Add.setOnClickListener {
            // Se acabamos de avaliar uma expressão, coloque o resultado antes da operação
            val str =   if (!evaluated)
                            text_calc.text.toString() + "+"
                        else {
                            evaluated = false
                            text_info.text.toString() + "+"
                        }
            text_calc.setText(str)
        }
        btn_Subtract.setOnClickListener {
            val str =   if (!evaluated)
                            text_calc.text.toString() + "-"
                        else {
                            evaluated = false
                            text_info.text.toString() + "-"
                        }
            text_calc.setText(str)
        }
        btn_Multiply.setOnClickListener {
            val str =   if (!evaluated)
                            text_calc.text.toString() + "*"
                        else {
                            evaluated = false
                            text_info.text.toString() + "*"
                        }
            text_calc.setText(str)
        }
        btn_Divide.setOnClickListener {
            val str =   if (!evaluated)
                            text_calc.text.toString() + "/"
                        else {
                            evaluated = false
                            text_info.text.toString() + "/"
                        }
            text_calc.setText(str)
        }
        btn_Power.setOnClickListener {
            val str =   if (!evaluated)
                            text_calc.text.toString() + "^"
                        else {
                            evaluated = false
                            text_info.text.toString() + "^"
                        }
            text_calc.setText(str)
        }
        // Limpa a string
        btn_Clear.setOnClickListener {
            text_calc.setText("")
            text_info.text = ""
            evaluated = false
        }
        btn_Equal.setOnClickListener {
            // Armazena o resultado em uma variável e a exibe
            invalid = false
            val res = eval(text_calc.text.toString())
            // Se a expressão a ser avaliada for invalida, limpe a tela
            if (invalid) btn_Clear.callOnClick()
            else {
                text_info.text = res.toString()
                evaluated = true
            }
        }
    }


    //Como usar a função:
    // eval("2+2") == 4.0
    // eval("2+3*4") = 14.0
    // eval("(2+3)*4") = 20.0
    //Fonte: https://stackoverflow.com/a/26227947
    fun eval(str: String): Double {
        return object : Any() {
            var pos = -1
            var ch: Char = ' '
            fun nextChar() {
                val size = str.length
                ch = if ((++pos < size)) str.get(pos) else (-1).toChar()
            }

            fun eat(charToEat: Char): Boolean {
                while (ch == ' ') nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < str.length)  {
                    // Exibe o Toast e limpa a tela
                    Toast.makeText(this@MainActivity, "Caractere inesperado: " + ch, Toast.LENGTH_LONG).show()
                    invalid = true
                    return 0.0
                }
                return x
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            // | number | functionName factor | factor `^` factor
            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    if (eat('+'))
                        x += parseTerm() // adição
                    else if (eat('-'))
                        x -= parseTerm() // subtração
                    else
                        return x
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    if (eat('*'))
                        x *= parseFactor() // multiplicação
                    else if (eat('/'))
                        x /= parseFactor() // divisão
                    else
                        return x
                }
            }

            fun parseFactor(): Double {
                if (eat('+')) return parseFactor() // + unário
                if (eat('-')) return -parseFactor() // - unário
                var x: Double
                val startPos = this.pos
                if (eat('(')) { // parênteses
                    x = parseExpression()
                    eat(')')
                } else if ((ch in '0'..'9') || ch == '.') { // números
                    while ((ch in '0'..'9') || ch == '.') nextChar()
                    x = java.lang.Double.parseDouble(str.substring(startPos, this.pos))
                } else if (ch in 'a'..'z') { // funções
                    while (ch in 'a'..'z') nextChar()
                    val func = str.substring(startPos, this.pos)
                    x = parseFactor()
                    if (func == "sqrt")
                        x = Math.sqrt(x)
                    else if (func == "sin")
                        x = Math.sin(Math.toRadians(x))
                    else if (func == "cos")
                        x = Math.cos(Math.toRadians(x))
                    else if (func == "tan")
                        x = Math.tan(Math.toRadians(x))
                    else {
                        // Exibe o Toast e limpa a tela
                        Toast.makeText(this@MainActivity, "Função desconhecida: " + func, Toast.LENGTH_LONG).show()
                        invalid = true
                        return 0.0
                    }
                } else {
                    // Exibe o Toast e limpa a tela
                    Toast.makeText(this@MainActivity, "Caractere inesperado: " + ch, Toast.LENGTH_LONG).show()
                    invalid = true
                    return 0.0
                }
                if (eat('^')) x = Math.pow(x, parseFactor()) // potência
                return x
            }
        }.parse()
    }
}
