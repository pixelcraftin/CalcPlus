package com.calculatorplus.app.data

data class UnitItem(
    val name: String,
    val symbol: String,
    val category: String,
    val factorToBase: Double, // Multiplier to get to base unit
    val offset: Double = 0.0 // Added offset (primarily for Temperature conversions)
)

object UnitData {
    val categories = listOf(
        "Angle", "Area", "Data", "Energy", 
        "Length", "Power", "Pressure", "Speed", "Temperature", 
        "Time", "Volume", "Weight"
    )

    val unitsMap = mapOf(
        "Angle" to listOf(
            UnitItem("Degrees", "°", "Angle", 1.0),
            UnitItem("Radians", "rad", "Angle", 180.0 / Math.PI),
            UnitItem("Gradians", "grad", "Angle", 0.9)
        ),
        "Area" to listOf(
            UnitItem("Square Millimeters", "mm²", "Area", 1e-6),
            UnitItem("Square Centimeters", "cm²", "Area", 1e-4),
            UnitItem("Square Meters", "m²", "Area", 1.0),
            UnitItem("Square Kilometers", "km²", "Area", 1e6),
            UnitItem("Square Inches", "in²", "Area", 0.00064516),
            UnitItem("Square Feet", "ft²", "Area", 0.09290304),
            UnitItem("Square Yards", "yd²", "Area", 0.83612736),
            UnitItem("Acres", "ac", "Area", 4046.8564224),
            UnitItem("Hectares", "ha", "Area", 10000.0)
        ),
        "Data" to listOf(
            UnitItem("Bits", "b", "Data", 0.125),
            UnitItem("Bytes", "B", "Data", 1.0),
            UnitItem("Kilobytes", "KB", "Data", 1024.0),
            UnitItem("Megabytes", "MB", "Data", 1048576.0),
            UnitItem("Gigabytes", "GB", "Data", 1073741824.0),
            UnitItem("Terabytes", "TB", "Data", 1099511627776.0),
            UnitItem("Petabytes", "PB", "Data", 1125899906842624.0)
        ),
        "Energy" to listOf(
            UnitItem("Joules", "J", "Energy", 1.0),
            UnitItem("Kilojoules", "kJ", "Energy", 1000.0),
            UnitItem("Calories", "cal", "Energy", 4.184),
            UnitItem("Kilocalories", "kcal", "Energy", 4184.0),
            UnitItem("Watt Hours", "Wh", "Energy", 3600.0),
            UnitItem("Kilowatt Hours", "kWh", "Energy", 3600000.0)
        ),
        "Length" to listOf(
            UnitItem("Millimeters", "mm", "Length", 0.001),
            UnitItem("Centimeters", "cm", "Length", 0.01),
            UnitItem("Meters", "m", "Length", 1.0),
            UnitItem("Kilometers", "km", "Length", 1000.0),
            UnitItem("Inches", "in", "Length", 0.0254),
            UnitItem("Feet", "ft", "Length", 0.3048),
            UnitItem("Yards", "yd", "Length", 0.9144),
            UnitItem("Miles", "mi", "Length", 1609.344)
        ),
        "Power" to listOf(
            UnitItem("Watts", "W", "Power", 1.0),
            UnitItem("Kilowatts", "kW", "Power", 1000.0),
            UnitItem("Horsepower", "hp", "Power", 745.699872)
        ),
        "Pressure" to listOf(
            UnitItem("Pascals", "Pa", "Pressure", 1.0),
            UnitItem("Kilopascals", "kPa", "Pressure", 1000.0),
            UnitItem("Bar", "bar", "Pressure", 100000.0),
            UnitItem("Pounds per Sq Inch", "psi", "Pressure", 6894.75729),
            UnitItem("Atmospheres", "atm", "Pressure", 101325.0)
        ),
        "Speed" to listOf(
            UnitItem("Meters / Second", "m/s", "Speed", 1.0),
            UnitItem("Kilometers / Hour", "km/h", "Speed", 1.0 / 3.6),
            UnitItem("Miles / Hour", "mph", "Speed", 0.44704),
            UnitItem("Knots", "kn", "Speed", 0.514444)
        ),
        "Temperature" to listOf(
            UnitItem("Celsius", "°C", "Temperature", 1.0, 0.0),
            UnitItem("Fahrenheit", "°F", "Temperature", 5.0 / 9.0, 32.0),
            UnitItem("Kelvin", "K", "Temperature", 1.0, 273.15)
        ),
        "Time" to listOf(
            UnitItem("Milliseconds", "ms", "Time", 0.001),
            UnitItem("Seconds", "s", "Time", 1.0),
            UnitItem("Minutes", "min", "Time", 60.0),
            UnitItem("Hours", "h", "Time", 3600.0),
            UnitItem("Days", "d", "Time", 86400.0),
            UnitItem("Weeks", "wk", "Time", 604800.0),
            UnitItem("Months", "mo", "Time", 2628000.0),
            UnitItem("Years", "yr", "Time", 31536000.0)
        ),
        "Volume" to listOf(
            UnitItem("Milliliters", "ml", "Volume", 0.001),
            UnitItem("Liters", "L", "Volume", 1.0),
            UnitItem("Cubic Centimeters", "cm³", "Volume", 0.001),
            UnitItem("Cubic Meters", "m³", "Volume", 1000.0),
            UnitItem("Teaspoons", "tsp", "Volume", 0.00492892),
            UnitItem("Tablespoons", "tbsp", "Volume", 0.01478676),
            UnitItem("Fluid Ounces", "fl oz", "Volume", 0.02957353),
            UnitItem("Cups", "cup", "Volume", 0.23658824),
            UnitItem("Pints", "pt", "Volume", 0.47317647),
            UnitItem("Quarts", "qt", "Volume", 0.94635295),
            UnitItem("Gallons", "gal", "Volume", 3.78541178),
            UnitItem("Oil Barrels", "bbl", "Volume", 158.987294928)
        ),
        "Weight" to listOf(
            UnitItem("Milligrams", "mg", "Weight", 1e-6),
            UnitItem("Grams", "g", "Weight", 0.001),
            UnitItem("Kilograms", "kg", "Weight", 1.0),
            UnitItem("Metric Tons", "t", "Weight", 1000.0),
            UnitItem("Ounces", "oz", "Weight", 0.02834952),
            UnitItem("Pounds", "lb", "Weight", 0.45359237),
            UnitItem("Stones", "st", "Weight", 6.35029318)
        )
    )
}
