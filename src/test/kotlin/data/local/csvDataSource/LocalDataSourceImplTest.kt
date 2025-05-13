package data.local.csvDataSource

import com.google.common.truth.Truth.assertThat
import data.local.csvDataSource.csv.CsvDataSourceImpl
import fake.*
import io.mockk.*
import kotlinx.datetime.LocalDateTime
import logic.entity.*
import logic.entity.type.UserRole
import logic.exceptions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class LocalDataSourceImplTest {

    private lateinit var auditCsvDataSource: CsvDataSourceImpl<Audit>
    private lateinit var userCsvDataSource: CsvDataSourceImpl<User>
    private lateinit var projectCsvDataSource: CsvDataSourceImpl<Project>
    private lateinit var taskCsvDataSource: CsvDataSourceImpl<Task>
    private lateinit var taskStateCsvDataSource: CsvDataSourceImpl<TaskState>
    private lateinit var localDataSourceImpl: LocalDataSourceImpl
    private lateinit var testUsers: List<User>

    private val testUser = createUser(
        userName = "mohamed",
        password = "5f4dcc3b5aa765d61d8327deb882cf99",
        role = UserRole.ADMIN
    )

    @BeforeEach
    fun setUp() {
        auditCsvDataSource = mockk(relaxed = true)
        userCsvDataSource = mockk(relaxed = true)
        projectCsvDataSource = mockk(relaxed = true)
        taskCsvDataSource = mockk(relaxed = true)
        taskStateCsvDataSource = mockk(relaxed = true)

        localDataSourceImpl = LocalDataSourceImpl(
            auditCsvDataSource,
            userCsvDataSource,
            projectCsvDataSource,
            taskCsvDataSource,
            taskStateCsvDataSource
        )

        testUsers = listOf(
            createUser(
                userName = "mohammed1234",
                password = "12345678"
            ),
            createUser(
                userName = "mohammed123",
                password = "12345678"
            ),
            createUser(
                userName = "mohammed121234",
                password = "12345678"
            ),
            createUser(
                userName = "mohammed123423424",
                password = "12345678"
            )
        )
    }

    //region Authentication
    @Test
    fun `getAuthenticatedUser should throw UserNotFoundException when user does not exist`() {
        // Given
        every { userCsvDataSource.loadAllDataFromFile() } returns emptyList()

        // When/Then
        assertThrows<UserNotFoundException> {
            localDataSourceImpl.getAuthenticatedUser("nonexistent", "password123")
        }
        verify(exactly = 1) { userCsvDataSource.loadAllDataFromFile() }
    }

    @Test
    fun `getAuthenticatedUser should throw InvalidPasswordException when password is incorrect`() {
        // Given
        val users = listOf(testUser)
        every { userCsvDataSource.loadAllDataFromFile() } returns users

        // When/Then
        assertThrows<InvalidPasswordException> {
            localDataSourceImpl.getAuthenticatedUser("mohamed", "wrongpassword")
        }
        verify(exactly = 1) { userCsvDataSource.loadAllDataFromFile() }
    }

    @Test
    fun `saveUser should add new user and return true when successful`() {
        // Given
        every { userCsvDataSource.loadAllDataFromFile() } returns emptyList()
        every { userCsvDataSource.appendToFile(any()) } returns Unit

        // When
        val result = localDataSourceImpl.saveUser("newuser", "password123", UserRole.MATE)

        // Then
        assertThat(result).isTrue()
        verify(exactly = 1) { userCsvDataSource.loadAllDataFromFile() }
        verify(exactly = 1) { userCsvDataSource.appendToFile(any()) }
    }

    @Test
    fun `saveUser should throw UserExistsException when username already exists`() {
        // Given
        every { userCsvDataSource.loadAllDataFromFile() } returns listOf(testUser)

        // When/Then
        assertThrows<UserExistsException> {
            localDataSourceImpl.saveUser("mohamed", "newpassword", UserRole.MATE)
        }
        verify(exactly = 1) { userCsvDataSource.loadAllDataFromFile() }
        verify(exactly = 0) { userCsvDataSource.appendToFile(any()) }
    }
    //endregion

    //region audit
    @Test
    fun `should return all audit logs from CSV`() {
        // Given
        val auditLogs = listOf(
            createAudit(
                userRole = UserRole.ADMIN,
                userName = "Admin",
                entityType = Audit.EntityType.PROJECT,
                entityId = "PROJECT-001",
                action = Audit.ActionType.CREATE,
            ),
            createAudit(
                userRole = UserRole.MATE,
                userName = "User1",
                entityType = Audit.EntityType.TASK,
                entityId = "TASK-123",
                action = Audit.ActionType.UPDATE,
            )
        )
        every { auditCsvDataSource.loadAllDataFromFile() } returns auditLogs

        // When
        val result = localDataSourceImpl.getAllAuditLogs()

        // Then
        assertThat(result).isEqualTo(auditLogs)
        verify { auditCsvDataSource.loadAllDataFromFile() }
    }

    @Test
    fun `should return empty list when CSV is empty`() {
        // Given
        every { auditCsvDataSource.loadAllDataFromFile() } returns emptyList()

        // When
        val result = localDataSourceImpl.getAllAuditLogs()

        // Then
        assertThat(result).isEmpty()
        verify { auditCsvDataSource.loadAllDataFromFile() }
    }

    @Test
    fun `should throw DataAccessException when CSV read fails`() {
        // Given
        every { auditCsvDataSource.loadAllDataFromFile() } throws DataAccessException("Failed to read audit.csv")

        // When&&Then
        val exception = assertThrows<DataAccessException> {
            localDataSourceImpl.getAllAuditLogs()
        }
        assertThat(exception.message).isEqualTo("Failed to read audit.csv")
        verify { auditCsvDataSource.loadAllDataFromFile() }
    }

    @Test
    fun `should append audit log to CSV`() {
        // Given
        val auditLog = createAudit(
            userRole = UserRole.ADMIN,
            userName = "Adel",
            entityType = Audit.EntityType.PROJECT,
            entityId = "PROJECT-001",
            action = Audit.ActionType.CREATE,
        )

        // When
        localDataSourceImpl.addAuditLog(auditLog)

        // Then
        verify { auditCsvDataSource.appendToFile(auditLog) }
    }

    @Test
    fun `should append audit log with state change to CSV`() {
        // Given
        val auditLog = createAudit(
            userRole = UserRole.MATE,
            userName = "User1",
            entityType = Audit.EntityType.TASK,
            entityId = "TASK-123",
            action = Audit.ActionType.UPDATE,
        )

        // When
        localDataSourceImpl.addAuditLog(auditLog)

        // Then
        verify { auditCsvDataSource.appendToFile(auditLog) }
    }

    @Test
    fun `should throw DataAccessException when CSV write fails`() {
        // Given
        val auditLog = createAudit(
            userRole = UserRole.ADMIN,
            userName = "Admin",
            entityType = Audit.EntityType.PROJECT,
            entityId = "PROJECT-002",
            action = Audit.ActionType.DELETE,
        )
        every { auditCsvDataSource.appendToFile(auditLog) } throws DataAccessException("Failed to write to audit.csv")

        // When&&Then
        val exception = assertThrows<DataAccessException> {
            localDataSourceImpl.addAuditLog(auditLog)
        }
        assertThat(exception.message).isEqualTo("Failed to write to audit.csv")
        verify { auditCsvDataSource.appendToFile(auditLog) }
    }

    @Test
    fun `should return audit logs for a specific project ID when provided`() {
        // Given
        val projectId = "PROJECT-001"
        val auditLogs = listOf(
            createAudit(
                userRole = UserRole.ADMIN,
                userName = "Admin",
                entityType = Audit.EntityType.PROJECT,
                entityId = projectId,
                action = Audit.ActionType.CREATE,
            ),
            createAudit(
                userRole = UserRole.MATE,
                userName = "User1",
                entityType = Audit.EntityType.TASK,
                entityId = "TASK-123",
                action = Audit.ActionType.UPDATE,
            ),
            createAudit(
                userRole = UserRole.ADMIN,
                userName = "Admin",
                entityType = Audit.EntityType.PROJECT,
                entityId = projectId,
                action = Audit.ActionType.UPDATE,
            )
        )
        every { auditCsvDataSource.loadAllDataFromFile() } returns auditLogs

        // When
        val result = localDataSourceImpl.getAuditLogsByProjectId(projectId)

        // Then
        assertThat(result.size).isEqualTo(2)
        assertThat(result.all { it.entityId == projectId && it.entityType == Audit.EntityType.PROJECT }).isTrue()
        assertThat(result.map { it.action }).containsExactly(Audit.ActionType.CREATE, Audit.ActionType.UPDATE)
        verify { auditCsvDataSource.loadAllDataFromFile() }
    }

    @Test
    fun `should return empty list when no audit logs match project ID`() {
        // Given
        val projectId = "PROJECT-999"
        val auditLogs = listOf(
            createAudit(
                userRole = UserRole.MATE,
                userName = "User1",
                entityType = Audit.EntityType.TASK,
                entityId = "TASK-123",
                action = Audit.ActionType.UPDATE,
            )
        )
        every { auditCsvDataSource.loadAllDataFromFile() } returns auditLogs

        // When
        val result = localDataSourceImpl.getAuditLogsByProjectId(projectId)

        // Then
        assertThat(result).isEmpty()
        verify { auditCsvDataSource.loadAllDataFromFile() }
    }

    @Test
    fun `should return audit logs for a specific task ID when provided`() {
        // Given
        val taskId = "TASK-123"
        val auditLogs = listOf(
            createAudit(
                userRole = UserRole.MATE,
                userName = "User1",
                entityType = Audit.EntityType.TASK,
                entityId = taskId,
                action = Audit.ActionType.UPDATE,
            ),
            createAudit(
                userRole = UserRole.ADMIN,
                userName = "Admin",
                entityType = Audit.EntityType.PROJECT,
                entityId = "PROJECT-001",
                action = Audit.ActionType.CREATE,
            ),
            createAudit(
                userRole = UserRole.MATE,
                userName = "User2",
                entityType = Audit.EntityType.TASK,
                entityId = taskId,
                action = Audit.ActionType.DELETE,
            )
        )
        every { auditCsvDataSource.loadAllDataFromFile() } returns auditLogs

        // When
        val result = localDataSourceImpl.getAuditLogsByTaskId(taskId)

        // Then
        assertThat(result.size).isEqualTo(2)
        assertThat(result.all { it.entityId == taskId && it.entityType == Audit.EntityType.TASK }).isTrue()
        assertThat(result.map { it.action }).containsExactly(Audit.ActionType.UPDATE, Audit.ActionType.DELETE)
        verify { auditCsvDataSource.loadAllDataFromFile() }
    }

    @Test
    fun `should return empty list when no audit logs match task ID`() {
        // Given
        val taskId = "TASK-999"
        val auditLogs = listOf(
            createAudit(
                userRole = UserRole.ADMIN,
                userName = "Admin",
                entityType = Audit.EntityType.PROJECT,
                entityId = "PROJECT-001",
                action = Audit.ActionType.CREATE,
            )
        )
        every { auditCsvDataSource.loadAllDataFromFile() } returns auditLogs

        // When
        val result = localDataSourceImpl.getAuditLogsByTaskId(taskId)

        // Then
        assertThat(result).isEmpty()
        verify { auditCsvDataSource.loadAllDataFromFile() }
    }

    @Test
    fun `should throw DataAccessException when CSV read fails for task ID`() {
        // Given
        val taskId = "TASK-123"
        every { auditCsvDataSource.loadAllDataFromFile() } throws DataAccessException("Failed to read audit.csv")

        // When&&Then
        val exception = assertThrows<DataAccessException> {
            localDataSourceImpl.getAuditLogsByTaskId(taskId)
        }
        assertThat(exception.message).isEqualTo("Failed to read audit.csv")
        verify { auditCsvDataSource.loadAllDataFromFile() }
    }
    //endregion

    //region project
    @Test
    fun `should return all projects from the csv data source when calling the getAllProjects function`() {
        // Given
        val project = createProject(name = "Project1")
        val expectedProjects = listOf(project)
        every { projectCsvDataSource.loadAllDataFromFile() } returns expectedProjects

        // When
        val result = localDataSourceImpl.getAllProjects()

        // Then
        assertThat(result).isEqualTo(expectedProjects)
    }

    @Test
    fun `should add a new project to the data source when project is valid`() {
        // Given
        val project = createProject(name = "NewProject")
        every { projectCsvDataSource.appendToFile(project) } just Runs

        // When
        localDataSourceImpl.addProject(project)

        // Then
        verify { projectCsvDataSource.appendToFile(project) }
    }

    @Test
    fun `should delete a project by ID`() {
        // Given
        val projectId = Uuid.random().toString()
        every { projectCsvDataSource.deleteById(projectId) } just Runs

        // When
        localDataSourceImpl.deleteProject(projectId)

        // Then
        verify { projectCsvDataSource.deleteById(projectId) }
    }

    @Test
    fun `should update a project by ID when it exists`() {
        // Given
        val newProjects = createProject()

        // When
        localDataSourceImpl.updateProject(newProjects)

        // Then
        verify {
            projectCsvDataSource.updateItem(newProjects)
        }
    }

    @Test
    fun `should throw exception when updating project that does not exist`() {
        // Given
        val newProjects = createProject()
        every {
            projectCsvDataSource.updateItem(newProjects)
        } throws DataWriteException("Error saving to CSV file")

        // When & Then
        assertThrows<DataWriteException> {
            localDataSourceImpl.updateProject(newProjects)
        }
    }
    //endregion

    //region task
    @Test
    fun `should return all tasks when getAllTasks is called`() {
        // Given
        val task1 = createTask(
            projectId = "project-1",
            title = "Task 1",
            description = "Description 1",
            createdBy = "user-1",
            stateId = "state-1",
            createdAt = LocalDateTime(2023, 1, 1, 0, 0),
            updatedAt = LocalDateTime(2023, 1, 1, 0, 0)
        )
        val task2 = createTask(
            projectId = "project-2",
            title = "Task 2",
            description = "Description 2",
            createdBy = "user-2",
            stateId = "state-2",
            createdAt = LocalDateTime(2023, 1, 1, 0, 0),
            updatedAt = LocalDateTime(2023, 1, 1, 0, 0)
        )
        val tasks = listOf(task1, task2)
        every { taskCsvDataSource.loadAllDataFromFile() } returns tasks

        // When
        val result = localDataSourceImpl.getAllTasks()

        // Then
        assertThat(result).containsExactly(task1, task2).inOrder()
        verify { taskCsvDataSource.loadAllDataFromFile() }
    }

    @Test
    fun `should return empty list when no tasks exist`() {
        // Given
        every { taskCsvDataSource.loadAllDataFromFile() } returns emptyList()

        // When
        val result = localDataSourceImpl.getAllTasks()

        // Then
        assertThat(result).isEmpty()
        verify { taskCsvDataSource.loadAllDataFromFile() }
    }

    @Test
    fun `should return task when getTaskById is called with valid ID`() {
        // Given
        val task = createTask(
            projectId = "project-1",
            title = "Test Task",
            description = "Test Description",
            createdBy = "user-1",
            stateId = "state-1",
            createdAt = LocalDateTime(2023, 1, 1, 0, 0),
            updatedAt = LocalDateTime(2023, 1, 1, 0, 0)
        )
        every { taskCsvDataSource.loadAllDataFromFile() } returns listOf(task)

        // When
        val result = localDataSourceImpl.getTaskById(task.id.toString())

        // Then
        assertThat(result).isEqualTo(task)
        verify { taskCsvDataSource.loadAllDataFromFile() }
    }

    @Test
    fun `should throw TaskNotFoundException when getTaskById is called with invalid ID`() {
        // Given
        every { taskCsvDataSource.loadAllDataFromFile() } returns emptyList()
        val invalidTaskId = kotlin.uuid.Uuid.random().toString()

        // When & Then
        assertFailsWith<TaskNotFoundException> {
            localDataSourceImpl.getTaskById(invalidTaskId)
        }

        verify { taskCsvDataSource.loadAllDataFromFile() }
    }

    @Test
    fun `should update task and call updateFile when updateTask is called with valid ID`() {
        // Given
        val originalTask = createTask(title = "Original Task")
        val updatedTask = originalTask.copy(title = "Updated Task")
        every { taskCsvDataSource.loadAllDataFromFile() } returns listOf(originalTask)
        every { taskCsvDataSource.updateFile(any()) } returns Unit

        // When
        val result = localDataSourceImpl.updateTask(updatedTask)

        // Then
        assertThat(result).isEqualTo(updatedTask)
        verify { taskCsvDataSource.loadAllDataFromFile() }
        verify { taskCsvDataSource.updateFile(listOf(updatedTask)) }
    }

    @Test
    fun `should throw TaskNotFoundException when updateTask is called with invalid ID`() {
        // Given
        val updatedTask = createTask(
            projectId = "project-1",
            title = "Updated Task",
            description = "Updated Description",
            createdBy = "user-1",
            stateId = "state-1",
            createdAt = LocalDateTime(2023, 1, 1, 0, 0),
            updatedAt = LocalDateTime(2023, 1, 1, 0, 0)
        )
        val taskId = updatedTask.id.toString()
        every { taskCsvDataSource.loadAllDataFromFile() } returns emptyList()

        // When
        val exception = assertFailsWith<TaskNotFoundException> {
            localDataSourceImpl.updateTask(updatedTask)
        }

        // Then
        assertThat(exception.message).isEqualTo("Task with ID $taskId not found")
        verify { taskCsvDataSource.loadAllDataFromFile() }
        verify(exactly = 0) { taskCsvDataSource.updateFile(any()) }
    }

    private val task1 = createTask(title = "First Task")
    private val task2 = createTask(title = "Second Task")

    @Test
    fun `addTask should append task to data source`() {
        // When
        localDataSourceImpl.addTask(task1)

        // Then
        verify { taskCsvDataSource.appendToFile(task1) }
    }

    @Test
    fun `deleteTask should remove task by id`() {
        // Given
        every { taskCsvDataSource.loadAllDataFromFile() } returns listOf(task1, task2)

        // When
        localDataSourceImpl.deleteTask(task1.id.toString())

        // Then
        verify { taskCsvDataSource.deleteById(task1.id.toString()) }
    }
    //endregion

    //region task state
    @Test
    fun `should add a new state to the data source`() {
        // Given
        val project = listOf(
            createProject(
                id = Uuid.parse("964801c9-49f6-4e7b-899b-113337a91848"),
                name = "PlanMate Core Features",
                createdBy = "adminUser01"
            )
        )
        val newState = createState(
            id = Uuid.parse("ae7095f0-4a95-466b-8ed6-6f54827268da"),
            name = "InProgress",
            projectId = project[0].id
        )

        taskStateCsvDataSource.appendToFile(newState)
        // When
        val result = localDataSourceImpl.addTaskState(newState)

        // Then
        assertThat(result).isTrue()
    }

    @Test
    fun `should return states filtered by project id`() {
        // Given
        val project = listOf(
            createProject(
                id = Uuid.parse("964801c9-49f6-4e7b-899b-113337a91848"),
                name = "PlanMate Core Features",
                createdBy = "adminUser01"
            )
        )
        val state1 = createState(
            id = Uuid.parse("ae7095f0-4a95-466b-8ed6-6f54827268db"),
            name = "Todo",
            projectId = project[0].id
        )
        val state2 = createState(
            id = Uuid.parse("be7095f0-4a95-466b-8ed6-6f54827268db"),
            name = "Done",
            projectId = project[0].id
        )
        every { taskStateCsvDataSource.loadAllDataFromFile() } returns listOf(state1, state2)

        // When
        val result = localDataSourceImpl.getTaskStatesByProjectId("964801c9-49f6-4e7b-899b-113337a91848")

        // Then
        assertThat(result).containsExactly(state1, state2)
    }

    @Test
    fun `should return state by id if it exists`() {
        // Given
        val project = listOf(
            createProject(
                id = Uuid.parse("ae7095f0-4a95-466b-8ed6-6f54827268da"),
                name = "PlanMate Core Features",
                createdBy = "adminUser01"
            )
        )
        val state = createState(
            id = Uuid.parse("964801c9-49f6-4e7b-899b-113337a91848"),
            name = "InProgress",
            projectId = project[0].id
        )
        every { taskStateCsvDataSource.loadAllDataFromFile() } returns listOf(state)

        // When
        val result = localDataSourceImpl.getTaskStateById("964801c9-49f6-4e7b-899b-113337a91848")

        // Then
        assertThat(result).isEqualTo(state)
    }

    @Test
    fun `should throw exception when state id does not exist`() {
        // Given
        every { taskStateCsvDataSource.loadAllDataFromFile() } returns emptyList()

        // When & Then
        assertThrows<StateNotFoundException> {
            localDataSourceImpl.getTaskStateById("non-existing-id")
        }
    }

    @Test
    fun `should throw exception when state id does not exist in non-empty data source`() {
        // Given
        val state = createState(
            id = Uuid.parse("12345678-1234-1234-1234-123456789012"),
            name = "Done",
            projectId = Uuid.random()
        )
        every { taskStateCsvDataSource.loadAllDataFromFile() } returns listOf(state)

        // When & Then
        val exception = kotlin.test.assertFailsWith<StateNotFoundException> {
            localDataSourceImpl.getTaskStateById("non-existing-id")
        }
        assertThat(exception).isInstanceOf(StateNotFoundException::class.java)
    }

    @Test
    fun `should delete an existing state`() {
        // Given
        val project = listOf(
            createProject(name = "PlanMate Core Features", createdBy = "adminUser01")
        )
        val state = createState(name = "Todo", projectId = project[0].id)
        val stateList = mutableListOf(state)
        every { taskStateCsvDataSource.loadAllDataFromFile() } returns stateList

        // When
        val result = localDataSourceImpl.deleteTaskState(state)

        // Then
        assertEquals(true, result)
    }

    @Test
    fun `should update an existing state`() {
        // Given
        val project = createProject(
            id = Uuid.parse("964801c9-49f6-4e7b-899b-113337a91848"),
            name = "PlanMate Core Features",
            createdBy = "adminUser01"
        )
        val existingState = createState(
            id = Uuid.parse("ae7095f0-4a95-466b-8ed6-6f54827268da"),
            name = "ToDo",
            projectId = project.id
        )
        val updatedState = existingState.copy(name = "InProgress")
        every { taskStateCsvDataSource.loadAllDataFromFile() } returns listOf(existingState)
        every { taskStateCsvDataSource.updateFile(any()) } returns Unit

        // When
        val result = localDataSourceImpl.updateTaskState(updatedState)

        // Then
        assertThat(result).isEqualTo(updatedState)
    }

    @Test
    fun `should throw exception when updating a non-existent state`() {
        // Given
        val nonExistentState = createState(
            id = Uuid.parse("123e4567-e89b-12d3-a456-426614174000"),
            name = "Done",
            projectId = Uuid.random()
        )
        every { taskStateCsvDataSource.loadAllDataFromFile() } returns emptyList()

        // When & Then
        assertThrows<StateNotFoundException> {
            localDataSourceImpl.updateTaskState(nonExistentState)
        }
    }
    //endregion

    //region user
    @Test
    fun `should return all users when data source has users`() {
        // Given
        every { userCsvDataSource.loadAllDataFromFile() } returns testUsers
        localDataSourceImpl = LocalDataSourceImpl(
            auditCsvDataSource,
            userCsvDataSource,
            projectCsvDataSource,
            taskCsvDataSource,
            taskStateCsvDataSource
        )

        // When
        val result = localDataSourceImpl.getAllUsers()

        // Then
        assertThat(result).isEqualTo(testUsers)
    }

    @Test
    fun `should return empty list when data source has no users`() {
        // Given
        every { userCsvDataSource.loadAllDataFromFile() } returns emptyList()
        localDataSourceImpl = LocalDataSourceImpl(
            auditCsvDataSource,
            userCsvDataSource,
            projectCsvDataSource,
            taskCsvDataSource,
            taskStateCsvDataSource
        )

        // When
        val result = localDataSourceImpl.getAllUsers()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should return user when user id is founded`() {
        // Given
        every { userCsvDataSource.loadAllDataFromFile() } returns testUsers
        localDataSourceImpl = LocalDataSourceImpl(
            auditCsvDataSource,
            userCsvDataSource,
            projectCsvDataSource,
            taskCsvDataSource,
            taskStateCsvDataSource
        )
        val firstUser = testUsers.first()

        // When
        val actual = localDataSourceImpl.getUserByUserId(firstUser.id.toString())

        // Then
        assertThat(actual).isEqualTo(firstUser)
    }

    @Test
    fun `should throw UserNotFoundException when user id is not found`() {
        // Given
        every { userCsvDataSource.loadAllDataFromFile() } returns testUsers
        localDataSourceImpl = LocalDataSourceImpl(
            auditCsvDataSource,
            userCsvDataSource,
            projectCsvDataSource,
            taskCsvDataSource,
            taskStateCsvDataSource
        )
        val nonExistentUserId = "non-existent-id"

        // When/Then
        assertThrows<UserNotFoundException> {
            localDataSourceImpl.getUserByUserId(nonExistentUserId)
        }
    }
    //endregion
}