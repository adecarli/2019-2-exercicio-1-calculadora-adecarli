package br.ufpe.cin.android.calculadora

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var evaluated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        text_calc.setText(savedInstanceState?.getString("text_calc_content"))
        text_info.text = savedInstanceState?.getString("text_info_content")
        evaluated = savedInstanceState?.getBoolean("evaluated") ?: false

        val digits = listOf(btn_0, btn_1, btn_2, btn_3, btn_4, btn_5,
                            btn_6, btn_7, btn_8, btn_9, btn_Dot, btn_LParen, btn_RParen)
        val opers = listOf(btn_Add, btn_Subtract, btn_Multiply, btn_Divide, btn_Power)

        for (button in digits) {
            // Adiciona o digito pressionado à string de exibição
            button.setOnClickListener {
                // Se acabamos de avaliar uma expressão, limpe e coloque apenas o digito pressionado
                val str =   if (!evaluated)
                    text_calc.text.toString() + button.text
                else {
                    evaluated = false
                    button.text
                }
                text_calc.setText(str)
            }
        }

        for (oper in opers) {
            oper.setOnClickListener {
                // Se acabamos de avaliar uma expressão, coloque o resultado antes da operação
                val str =   if (!evaluated)
                    text_calc.text.toString() + oper.text
                else {
                    evaluated = false
                    text_info.text.toString() + oper.text
                }
                text_calc.setText(str)
            }
        }

        // Limpa a string
        btn_Clear.setOnClickListener {
            text_calc.setText("")
            text_info.text = ""
            evaluated = false
        }
        btn_Equal.setOnClickListener {
            // Armazena o resultado em uma variável e a exibe
            try {
                val res = eval(text_calc.text.toString())
                text_info.text = res.toString()
                evaluated = true
            } catch (e : java.lang.RuntimeException) {
                // Caso a string seja invalida, exibe o toast e limpa o display
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                btn_Clear.callOnClick()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("text_calc_content", text_calc.text.toString())
        outState.putString("text_info_content", text_info.text.toString())
        outState.putBoolean("evaluated", evaluated)
        super.onSaveInstanceState(outState)
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
                } else
                    throw RuntimeException("Caractere inesperado: " + ch)
                if (eat('^')) x = Math.pow(x, parseFactor()) // potência
                return x
            }
        }.parse()
    }
}
