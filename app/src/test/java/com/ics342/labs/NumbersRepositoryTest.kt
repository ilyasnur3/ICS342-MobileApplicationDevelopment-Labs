package com.ics342.labs

import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.random.Random

internal class NumbersRepositoryTest {

    @Test
    fun `If database does not have a number fetch it from the Api`() {
        // Setup
        val database = mockk<Database>()
        val api = mockk<Api>()
        val number = Number(UUID.randomUUID().toString(), Random.nextInt())
        val id = number.id

        every { database.getNumber(id) } returns null
        every { api.getNumber(id) } returns number

        // Act
        val repository = NumbersRepository(database, api)
        val result = repository.getNumber(id)

        // Assert
        assertNotNull(result)
        assertEquals(result, number)

        verify { database.getNumber(id) }
        verify { api.getNumber(id) }
    }

    @Test
    fun ifDatabaseIsEmptyShouldFetchNumbersFromApi() {
        // Setup
        val database = mockk<Database>()
        val api = mockk<Api>()
        val numbersFromApi = listOf(
            Number(UUID.randomUUID().toString(), Random.nextInt()),
            Number(UUID.randomUUID().toString(), Random.nextInt()),
            Number(UUID.randomUUID().toString(), Random.nextInt())
        )

        every { database.getAllNumbers() } returns emptyList() // Mock database to return an empty list
        every { api.getNumbers() } returns numbersFromApi // Mock API to return numbers
        every { database.storeNumbers(numbersFromApi) } just runs // Mock database.storeNumbers to do nothing

        // Act
        val repository = NumbersRepository(database, api)
        val result = repository.fetchNumbers()

        // Assert
        assertNotNull(result)
        assertEquals(numbersFromApi, result)

        // Verify that the required functions were called exactly once
        verify(exactly = 1) { database.getAllNumbers() }
        verify(exactly = 1) { api.getNumbers() }
        verify(exactly = 1) { database.storeNumbers(numbersFromApi) }
    }
}
