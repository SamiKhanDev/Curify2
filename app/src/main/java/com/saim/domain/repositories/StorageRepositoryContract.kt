package com.saim.domain.repositories

/**
 * Abstraction for storage operations such as uploading a file and obtaining a public URL.
 */
interface StorageRepositoryContract {
    fun uploadFile(filePath: String, onComplete: (Boolean, String?) -> Unit)
}

