package com.nanotech.app_test.data.errors

import java.io.IOException

class NoNetworkError(override val message: String = "Network not available") : IOException(message)