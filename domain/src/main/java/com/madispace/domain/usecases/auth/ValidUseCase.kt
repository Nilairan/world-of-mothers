package com.madispace.domain.usecases.auth

import android.util.Patterns
import com.madispace.domain.exceptions.EmailValidException
import com.madispace.domain.exceptions.NotImplementCustomFunction
import com.madispace.domain.exceptions.PassValidException
import com.madispace.domain.exceptions.PhoneValidException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.regex.Pattern

class ValidData private constructor(
    val validMap: Set<ValidField>
) {

    class Builder {
        private val validMap: HashSet<ValidField> = hashSetOf()

        fun addField(
                field: String,
                rule: Rule,
                errorBlock: () -> Unit
        ): Builder {
            validMap.add(ValidField(
                    field = field,
                    rule = rule,
                    errorBlock = errorBlock,
                    validateBlock = null
            ))
            return this
        }

        fun addField(
                field: String,
                errorBlock: () -> Unit,
                validateBlock: () -> Boolean
        ): Builder {
            validMap.add(ValidField(
                    field = field,
                    rule = CustomRule,
                    errorBlock = errorBlock,
                    validateBlock = validateBlock
            ))
            return this
        }

        fun build(): ValidData {
            return ValidData(validMap)
        }
    }

    companion object {
        const val DEFAULT_MIN_PASS_SIZE = 8
        const val DEFAULT_MIN_PHONE_SIZE = 10
    }
}

data class ValidField(
        val field: String,
        val rule: Rule,
        val errorBlock: () -> Unit,
        val validateBlock: (() -> Boolean)?
)

sealed class Rule
class EmailRule(val regex: String = Patterns.EMAIL_ADDRESS.pattern()) : Rule()
class PassRule(val minSize: Int = ValidData.DEFAULT_MIN_PASS_SIZE) : Rule()
class PhoneRule(val minSize: Int = ValidData.DEFAULT_MIN_PHONE_SIZE) : Rule()
object CustomRule : Rule()

interface ValidUseCase {
    /**
     * @throws EmailValidException for not valid email
     * @throws PassValidException for not valid password
     * @throws PhoneValidException for not valid email
     * @throws NotImplementCustomFunction on custom function null or throw exception
     */
    operator fun invoke(fields: ValidData): Flow<Boolean>
}

class ValidUseCaseImpl : ValidUseCase {
    override fun invoke(fields: ValidData): Flow<Boolean> {
        return flow {
            var isValid = true
            fields.validMap.forEach { field ->
                when (field.rule) {
                    is EmailRule -> {
                        val validateEmail = Pattern.compile(field.rule.regex).matcher(field.field).matches()
                        if (validateEmail.not()) {
                            isValid = false
                            field.errorBlock.invoke()
                        }
                    }
                    is PassRule -> {
                        val validatePass = field.field.length >= field.rule.minSize
                        if (validatePass.not()) {
                            isValid = false
                            field.errorBlock.invoke()
                        }
                    }
                    is PhoneRule -> {
                        val validatePhone = field.field.length >= field.rule.minSize
                        if (validatePhone.not()) {
                            isValid = false
                            field.errorBlock.invoke()
                        }
                    }
                    is CustomRule -> {
                        field.validateBlock?.let {
                            val validateCustom = it.invoke()
                            if (validateCustom.not()) {
                                isValid = false
                                field.errorBlock.invoke()
                            }
                        } ?: run {
                            throw NotImplementCustomFunction()
                        }
                    }
                }
            }
            emit(isValid)
        }
    }
}