package com.madispace.domain.usecases.auth

import com.madispace.domain.exceptions.EmailValidException
import com.madispace.domain.exceptions.NotImplementCustomFunction
import com.madispace.domain.exceptions.PassValidException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ValidData private constructor(
    val validMap: Set<ValidField>
) {

    class Builder {
        private val validMap: HashSet<ValidField> = hashSetOf()

        fun addField(field: String, rule: Rule): Builder {
            validMap.add(ValidField(field = field, rule = rule, function = null))
            return this
        }

        fun addField(field: String, function: () -> Boolean): Builder {
            validMap.add(ValidField(field = field, rule = CustomRule, function = function))
            return this
        }

        fun build(): ValidData {
            return ValidData(validMap)
        }
    }

    companion object {
        const val DEFAULT_EMAIL_REGEX =
            "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"
        const val DEFAULT_MIN_PASS_SIZE = 6
    }
}

data class ValidField(
        val field: String,
        val rule: Rule,
        var function: (() -> Boolean)?
)

sealed class Rule
class EmailRule(val regex: String = ValidData.DEFAULT_EMAIL_REGEX) : Rule()
class PassRule(val minSize: Int = ValidData.DEFAULT_MIN_PASS_SIZE) : Rule()
object CustomRule : Rule()

interface ValidUseCase {
    /**
     * @throws EmailValidException for not valid email
     * @throws PassValidException for not valid password
     * @throws NotImplementCustomFunction on custom function null or throw exception
     */
    operator fun invoke(fields: ValidData): Flow<Boolean>
}

class ValidUseCaseImpl : ValidUseCase {
    override fun invoke(fields: ValidData): Flow<Boolean> {
//        var isValid = true
//        return Single.just(true)
//            .map {
//                fields.validMap.forEach { item ->
//                    when (item.rule) {
//                        is EmailRule -> {
//                            isValid = Pattern.compile(item.rule.regex).matcher(item.field).matches()
//                            if (isValid.not()) {
//                                throw EmailValidException()
//                            }
//                        }
//                        is PassRule -> {
//                            isValid = item.field.length >= item.rule.minSize
//                            if (isValid.not()) {
//                                throw PassValidException()
//                            }
//                        }
//                        is CustomRule -> {
//                            item.function?.let {
//                                try {
//                                    isValid = it.invoke()
//                                    if (isValid.not()) {
//                                        throw CustomFunctionException(item.field)
//                                    }
//                                } catch (e: Exception) {
//                                    throw NotImplementCustomFunction()
//                                }
//                            } ?: run {
//                                throw NotImplementCustomFunction()
//                            }
//                        }
//                    }
//                }
//                return@map isValid
//            }
//            .subscribeOn(Schedulers.newThread())
        return flow<Boolean> { emit(true) }
    }
}