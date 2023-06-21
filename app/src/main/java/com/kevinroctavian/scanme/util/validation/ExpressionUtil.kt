package com.kevinroctavian.scanme.util.validation

import android.R.id.input
import java.util.LinkedList
import java.util.regex.Matcher
import java.util.regex.Pattern


object ExpressionUtil {

    /**
     * the input is not valid if...
     * ...stringNum is not number
     */
    fun isNumeric(stringNum: String?): Boolean {
        if (stringNum == null) {
            return false
        }
        try {
            val d = stringNum.toDouble()
        } catch (nfe: NumberFormatException) {
            return false
        }
        return true
    }

    /**
     * the input is not valid if...
     * ...stringNum is empty.
     * ...stringNum is null.
     * ...stringNum is not (*, +, - , /)
     */
    fun validateOperator(operator: String?): Boolean {
        if (operator == null) return false
        if (operator.isEmpty()) return false
        return operator == "+" || operator == "*" || operator == "-" || operator == "/"
    }

    /**
     * the input is not valid if...
     * ...operand1 is not number
     * ...operand2 is not number
     * ...operator is not arithmatic operator
     * ...divided by zero
     */
    fun validateArithmaticOperation(
        operand1: String,
        operand2: String,
        operator: String
    ): Boolean {
        if (!isNumeric(operand1)) return false
        if (!isNumeric(operand2)) return false
        if (!validateOperator(operator)) return false
        if (operator == "/" && operand2 == "0") return false
        return true
    }

    /**
     * the input is not valid if...
     * ...expression is empty
     * ...expression is contains alfabetic
     * ...operator is not exist
     * ...operand1 is not number
     * ...operand2 is not number
     * ...operand1 is does not exist
     * ...operand2 is does not exist
     * ... return is valid  if size >= 3 or list contains (operand1, operator, operand2)
     */
    fun extractArgumentAndExpresions(expression: String?): List<String> {
        val list: LinkedList<String> = LinkedList<String>()
        if (expression == null) return list
        if (expression.isEmpty()) return list
        if (expression.lowercase().contains("[a-z]".toRegex())) return list

        val regex = "(\\d+\\d+)|(\\d+)|([+-/*///^])|([/(/)])"
        val m: Matcher = Pattern.compile(regex).matcher(expression)
        while (m.find()) {
            list.add(m.group())
        }
        return list
    }
}