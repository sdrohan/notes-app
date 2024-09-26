package utils

// You can add more categories in here
val categories = setOf("Home", "College")

// You can also refactor this function to use Lambdas
fun isValidCategory(categoryToCheck: String?): Boolean {
    for (category in categories) {
        if (category.equals(categoryToCheck, ignoreCase = true)) {
            return true
        }
    }
    return false
}
