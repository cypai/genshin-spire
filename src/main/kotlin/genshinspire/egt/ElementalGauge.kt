package genshinspire.egt

data class ElementalGauge(val element: Element, val units: Float) {
    fun taxed(): ElementalGauge {
        return ElementalGauge(element, units * 0.8f)
    }
}