package com.kevinroctavian.scanme.util.validation

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ExpressionUtilTest{

    @Test
    fun `empty is numeric return false`(){
        val result = ExpressionUtil.isNumeric("")
        assertThat(result).isFalse()
    }

    @Test
    fun `null is numeric return false`(){
        val result = ExpressionUtil.isNumeric(null)
        assertThat(result).isFalse()
    }

    @Test
    fun `digits is numeric return true`(){
        val result = ExpressionUtil.isNumeric("22")
        assertThat(result).isTrue()
    }

    @Test
    fun `decimal is numeric return true`(){
        val result = ExpressionUtil.isNumeric("22.2")
        assertThat(result).isTrue()
    }

    @Test
    fun `minus is numeric return true`(){
        val result = ExpressionUtil.isNumeric("-22")
        assertThat(result).isTrue()
    }

    @Test
    fun `operator is empty return false`(){
        val result = ExpressionUtil.validateOperator("")
        assertThat(result).isFalse()
    }

    @Test
    fun `operator is null return false`(){
        val result = ExpressionUtil.validateOperator(null)
        assertThat(result).isFalse()
    }

    @Test
    fun `operator is not plus, min, divided by, multiply return false`(){
        val result = ExpressionUtil.validateOperator("%")
        assertThat(result).isFalse()
    }

    @Test
    fun `operator is plus, min, divided by, multiply return true`(){
        val result = ExpressionUtil.validateOperator("*")
        assertThat(result).isTrue()
    }

    @Test
    fun `operand1 is empty return false`(){
        val result = ExpressionUtil.validateArithmaticOperation("", "2", "+")
        assertThat(result).isFalse()
    }

    @Test
    fun `operand1 is not number return false`(){
        val result = ExpressionUtil.validateArithmaticOperation("a", "2", "+")
        assertThat(result).isFalse()
    }

    @Test
    fun `operand2 is empty return false`(){
        val result = ExpressionUtil.validateArithmaticOperation("12", "", "+")
        assertThat(result).isFalse()
    }

    @Test
    fun `operand2 is not number return false`(){
        val result = ExpressionUtil.validateArithmaticOperation("12", "a", "+")
        assertThat(result).isFalse()
    }

    @Test
    fun `operator is divided by and operand2 is zero return false`(){
        val result = ExpressionUtil.validateArithmaticOperation("2", "0", "/")
        assertThat(result).isFalse()
    }

    @Test
    fun `valid operand1, operand2 and operator return true`(){
        val result = ExpressionUtil.validateArithmaticOperation("2", "5", "+")
        assertThat(result).isTrue()
    }

    @Test
    fun `invalid expression alfabetic AB+CD`(){
        val result = ExpressionUtil.extractArgumentAndExpresions("AB+CD")
        assertThat(result.size >= 3).isFalse()
    }

    @Test
    fun `invalid expression no operator 112032`(){
        val result = ExpressionUtil.extractArgumentAndExpresions("112032")
        assertThat(result.size >= 3).isFalse()
    }

    @Test
    fun `invalid expression contains alfabetic 11A+2B`(){
        val result = ExpressionUtil.extractArgumentAndExpresions("11A+2B")
        assertThat(result.size >= 3).isFalse()
    }

    @Test
    fun `invalid expression invalid operator 1&2`(){
        val result = ExpressionUtil.extractArgumentAndExpresions("1&2")
        assertThat(result.size >= 3).isFalse()
    }

    @Test
    fun `invalid expression invalid no operand2 100+`(){
        val result = ExpressionUtil.extractArgumentAndExpresions("100+")
        assertThat(result.size >= 3).isFalse()
    }

    @Test
    fun `invalid expression invalid no operand1 +100`(){
        val result = ExpressionUtil.extractArgumentAndExpresions("+100")
        assertThat(result.size >= 3).isFalse()
    }

    @Test
    fun `valid expression 100+50`(){
        val result = ExpressionUtil.extractArgumentAndExpresions("100+50")
        assertThat(result.size >= 3).isTrue()
    }

    @Test
    fun `valid expression 100*50`(){
        val result = ExpressionUtil.extractArgumentAndExpresions("100*50")
        assertThat(result.size >= 3).isTrue()
    }

    @Test
    fun `valid expression more operators or operands 100+50+102-504`(){
        val result = ExpressionUtil.extractArgumentAndExpresions("100+50+102-504")
        assertThat(result.size >= 3).isTrue()
    }
}