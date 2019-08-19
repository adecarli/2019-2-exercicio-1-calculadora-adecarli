package br.ufpe.cin.android.calculadora

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Adiciona o dígito pressionado à string de exibição
        btn_0.setOnClickListener {
            val str = text_calc.text.toString() + "0"
            text_calc.setText(str)
        }
        btn_1.setOnClickListener {
            val str = text_calc.text.toString() + "1"
            text_calc.setText(str)
        }
        btn_2.setOnClickListener {
            val str = text_calc.text.toString() + "2"
            text_calc.setText(str)
        }
        btn_3.setOnClickListener {
            val str = text_calc.text.toString() + "3"
            text_calc.setText(str)
        }
        btn_4.setOnClickListener {
            val str = text_calc.text.toString() + "4"
            text_calc.setText(str)
        }
        btn_5.setOnClickListener {
            val str = text_calc.text.toString() + "5"
            text_calc.setText(str)
        }
        btn_6.setOnClickListener {
            val str = text_calc.text.toString() + "6"
            text_calc.setText(str)
        }
        btn_7.setOnClickListener {
            val str = text_calc.text.toString() + "7"
            text_calc.setText(str)
        }
        btn_8.setOnClickListener {
            val str = text_calc.text.toString() + "8"
            text_calc.setText(str)
        }
        btn_9.setOnClickListener {
            val str = text_calc.text.toString() + "9"
            text_calc.setText(str)
        }
        btn_Add.setOnClickListener {
            val str = text_calc.text.toString() + "+"
            text_calc.setText(str)
        }
        btn_Subtract.setOnClickListener {
            val str = text_calc.text.toString() + "-"
            text_calc.setText(str)
        }
        btn_Multiply.setOnClickListener {
            val str = text_calc.text.toString() + "*"
            text_calc.setText(str)
        }
        btn_Divide.setOnClickListener {
            val str = text_calc.text.toString() + "/"
            text_calc.setText(str)
        }
        btn_Dot.setOnClickListener {
            val str = text_calc.text.toString() + "."
            text_calc.setText(str)
        }
        btn_LParen.setOnClickListener {
            val str = text_calc.text.toString() + "("
            text_calc.setText(str)
        }
        btn_RParen.setOnClickListener {
            val str = text_calc.text.toString() + ")"
            text_calc.setText(str)
        }
        btn_Power.setOnClickListener {
            val str = text_calc.text.toString() + "^"
            text_calc.setText(str)
        }
        // Limpa a string
        btn_Clear.setOnClickListener {
            val str = ""
            text_calc.setText(str)
        }
        btn_Equal.setOnClickListener {
            TODO("Equal Button not yet implemented")

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
                if (pos < str.length) throw RuntimeException("Caractere inesperado: " + ch)
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
                    else
                        throw RuntimeException("Função desconhecida: " + func)
                } else {
                    throw RuntimeException("Caractere inesperado: " + ch.toChar())
                }
                if (eat('^')) x = Math.pow(x, parseFactor()) // potência
                return x
            }
        }.parse()
    }
}
