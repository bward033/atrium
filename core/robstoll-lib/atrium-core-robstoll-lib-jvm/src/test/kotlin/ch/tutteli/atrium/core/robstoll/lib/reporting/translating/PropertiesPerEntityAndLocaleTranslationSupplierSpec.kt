package ch.tutteli.atrium.core.robstoll.lib.reporting.translating

import ch.tutteli.atrium.domain.builders.ExpectImpl
import ch.tutteli.atrium.spec.reporting.translating.TranslatorIntSpec
import ch.tutteli.atrium.verbs.internal.AssertionVerbFactory

object PropertiesPerEntityAndLocaleTranslationSupplierSpec : TranslatorIntSpec(
    AssertionVerbFactory,
    { primaryLocale, fallbackLocales ->
        ExpectImpl.reporterBuilder
            .withTranslationSupplier(PropertiesPerEntityAndLocaleTranslationSupplier())
            .withDefaultLocaleOrderDecider()
            .withDefaultTranslator(primaryLocale, *fallbackLocales)
            .withDetailedObjectFormatter()
            .withDefaultAssertionFormatterController()
            .withDefaultAssertionFormatterFacade()
            .withTextSameLineAssertionPairFormatter()
            .withTextCapabilities()
            .withDefaultAtriumErrorAdjusters()
            .withOnlyFailureReporter()
            .build()
    },
    true,
    "[Atrium's TranslationIntSpec] "
)
