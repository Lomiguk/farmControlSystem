# Farm control system - WEB REST API
Веб-API на Java – системы контроля работ на ферме. Два типа пользователей администратор и работник (пользователь).
Работники собирают продукцию предприятия и вносят свои действия в журнал. Продукция может измеряться в штуках, килограммах и литрах.
Продукцию можно добавлять и убирать. Из результатам работы можно сформировать статистику,
так же итоги дня ежедневно приходят на почту. Так же работники могут получать оценки своей работы за день и задачи на определённый период.

Для рботы приложения требуется база данных PostgreSQL с названием farm_system 
(подключение по умолчанию: postgres, postgres)
Порт приложения: 8080 (по умолчанию)

Стек технологий: Java 17, Maven, PostgreSQL, Spring Boot 3, Spring Security, Jdbc, LIquibase, Open API, JWT, Hibernate validation, Lombok

Реализованные задачи:
 * Регистрация пользователя + ролевая система
 * Удаление/отключение аккауната пользователя
 * Регистрация продукта
 * Регистрация результатов работы
 * Формировнаие результатов работы/статистики
 * Ежедневная отправка результатов работы на почту
 * Оценки работников за день
Не реализоваанные задачи:
 *  Постановка задач

Описание REST-api:
Authentication [/auth]
  POST [/auth/sign-up] - регистрация пользователся с правами уровня USER
    URL example: localhost:8081/auth/sign-up
		Payload: SignUpRequest(input), ProfileResponse(output)
		Response: 200 OK (Успешная регистрация)
			        400 Bad Request (Ошибка верификации данных)
           
  POST [/auth/sign-in] - регистрация пользователся с правами уровня USER
    URL example: localhost:8081/auth/sign-in
		Payload: SignInRequest(input), JwtAuthenticationResponse(output)
		Response: 200 OK (Умпешная аутентификация)
			        400 Bad Request (Ошибка верификации данных)      
  DELETE [/auth/logout] - инвалидирует токены аутентифицированного пользователя
  	URL example: localhost:8081/auth/logout
  	Response: 200 OK (Успешное удаление токенов пользователя)
  				  401 BadRequest (Нет авторизванного пользовател)
  POST [/auth/refresh] - запрос нового токена доступа
    URL example: localhost:8081/auth/refresh
		Payload: String(input), JwtAuthenticationResponse(output)
		Response: 200 OK (Успешное обновление токена доступа)
			        400 Bad Request (Ошибка верификации данных)    

ProfileController [/profile]
  POST [] - создание профиля
    URL example: localhost:8081/profile
		Payload: AddProfileRequest(input), ProfileResponse(output)
		Response: 200 OK (Успешное создание профиля)
			        400 Bad Request (Ошибка верификации данных)
  GET [/{id}] - получение пользователя по id
    URL example: localhost:8081/profile/{id}
		Payload: Long(input), ProfileResponse(output)
		Response: 200 OK (Успешное получение данных о профиле)
			        400 Bad Request (Ошибка верификации данных)
  GET [/all] - получение пользователей
    URL example: localhost:8081/profile/all
		Payload: Integer Integer(input), ProfileResponse(output)
		Response: 200 OK (Успешное получение данных о профилях)
			        400 Bad Request (Ошибка верификации данных) 
  PATCH [/{id}/info] - Изменение фио профиля
    URL example: localhost:8081/profile/{id}/info
		Payload: Long String(input), ProfileResponse(output)
		Response: 200 OK (Успешное изменение фио пользователя)
			        400 Bad Request (Ошибка верификации данных)
  PATCH [/{id}/password] - Изменение пароля профиля
    URL example: localhost:8081/profile/{id}/password
		Payload: Long UpdatePasswordRequest(input), ProfileResponse(output)
		Response: 200 OK (Успешное изменение пароля пользователя)
			        400 Bad Request (Ошибка верификации данных)
  PATCH [/{id}/role] - Изменение роли профиля
    URL example: localhost:8081/profile/{id}/role
		Payload: Long Role(input), ProfileResponse(output)
		Response: 200 OK (Успешное изменение роли пользователя)
			        400 Bad Request (Ошибка верификации данных)
  PATCH [/{id}/actuality] - Изменение статуса актуальности профиля
    URL example: localhost:8081/profile/{id}/actuality
		Payload: Long Boolean(input), ProfileResponse(output)
		Response: 200 OK (Успешное изменение актуальности пользователя)
			        400 Bad Request (Ошибка верификации данных)
  PUT [/{id}] - Изменение данных профиля
    URL example: localhost:8081/profile/{id}
		Payload: Long UpdateProfileRequest(input), ProfileResponse(output)
		Response: 200 OK (Успешное изменение данных пользователя)
			        400 Bad Request (Ошибка верификации данных)
  DELET [/{id}] - Удаление или деактивация профиля
    URL example: localhost:8081/profile/{id}
		Payload: Long(input), Boolean(output)
		Response: 200 OK (Успешное удаения или деактивация пользователя)
			        400 Bad Request (Ошибка верификации данных)

ProductController [/product]
  POST [] - создание продукта
    URL example: localhost:8081/product
		Payload: AddProductRequest(input), ProductResponse(output)
		Response: 200 OK (Успешное создание продукта)
			        400 Bad Request (Ошибка верификации данных)
  GET [/{id}] - получение продукта по id
    URL example: localhost:8081/product/{id}
		Payload: Long(input), ProductResponse(output)
		Response: 200 OK (Успешное получение данных о продукте)
			        400 Bad Request (Ошибка верификации данных)
  GET [] - получение продукта по имени
    URL example: localhost:8081/product/
		Payload: String(input), ProductResponse(output)
		Response: 200 OK (Успешное получение данных о продукте)
			        400 Bad Request (Ошибка верификации данных)
  GET [/all] - получение продуктов
    URL example: localhost:8081/product/all
		Payload: Integer Integer(input), ProductResponse(output)
		Response: 200 OK (Успешное получение данных о продуктах)
			        400 Bad Request (Ошибка верификации данных)
  PATCH [/{id}/name] - Изменение наименования продутка
    URL example: localhost:8081/profile/{id}/name
		Payload: Long String(input), ProductResponse(output)
		Response: 200 OK (Успешное изменение наименование продутка)
			        400 Bad Request (Ошибка верификации данных)
  PATCH [/{id}/value-type] - Изменение типа измерения кол-ва продутка
    URL example: localhost:8081/product/{id}/value-type
		Payload: Long ValueType(input), ProductResponse(output)
		Response: 200 OK (Успешное изменение наименование продутка)
			        400 Bad Request (Ошибка верификации данных)
  PATCH [/{id}/actual-status] - Изменение типа статуса продутка
    URL example: localhost:8081/product/{id}/actual-status
		Payload: Long Boolean(input), ProductResponse(output)
		Response: 200 OK (Успешное изменение типа статуса продутка)
			        400 Bad Request (Ошибка верификации данных)
  PUT [/{id}] - Изменение данных продукта
    URL example: localhost:8081/product/{id}
		Payload: Long UpdateProductRequest(input), ProductResponse(output)
		Response: 200 OK (Успешное изменение данных продукта)
			        400 Bad Request (Ошибка верификации данных)
  DELET [/{id}] - Удаление или деактивация продутка
    URL example: localhost:8081/product/{id}
		Payload: Long, Boolean(output)
		Response: 200 OK (Успешное удаоение или деактивация продукта)
			        400 Bad Request (Ошибка верификации данных)

ProductController [/action]
  *Действие* – отмечаемое в журнале действие работника
  POST [] - создание "действия"
    URL example: localhost:8081/action
		Payload: AddActionRequest(input), ActionResponse(output)
		Response: 200 OK (Успешное создание "действия")
			        400 Bad Request (Ошибка верификации данных)
  GET [/{id}] - получение "действия" по id
    URL example: localhost:8081/action/{id}
		Payload: Long(input), ActionResponse(output)
		Response: 200 OK (Успешное получение данных о "действии")
			        400 Bad Request (Ошибка верификации данных)
  GET [/period] - получение "действия" за период
    URL example: localhost:8081/action/period
		Payload: Integer Integer PeriodRequest(input), Collection<ActionResponse>(output)
		Response: 200 OK (Успешное получение данных о "действии")
			        400 Bad Request (Ошибка верификации данных)
  PATCH [/{id}/profile] - Изменение id ответственного профиля
    URL example: localhost:8081/action/profile
		Payload: Long Long(input), ActionResponse(output)
		Response: 200 OK (Успешное изменение id ответственного профиля продутка)
			        400 Bad Request (Ошибка верификации данных)
  PATCH [/{id}/product] - Изменение id продутка
    URL example: localhost:8081/action/product
		Payload: Long Long Float(input), ActionResponse(output)
		Response: 200 OK (Успешное изменение id  продутка)
			        400 Bad Request (Ошибка верификации данных)
  PATCH [/{id}/actual-status] - Изменение статуса "действия"
    URL example: localhost:8081/action/{id}/actual-status
		Payload: Long Boolean(input), ActionResponse(output)
		Response: 200 OK (Успешное изменение id  продутка)
			        400 Bad Request (Ошибка верификации данных)
  PUT [/{id}] - Изменение данных "дейсивия"
    URL example: localhost:8081/action/{id}
		Payload: Long UpdateActionRequest(input), ActionResponse(output)
		Response: 200 OK (Успешное изменение данных продукта)
			        400 Bad Request (Ошибка верификации данных)
  DELET [/{id}] - Удаление или деактивация действия
    URL example: localhost:8081/action/{id}
		Payload: Long, Boolean(output)
		Response: 200 OK (Успешное удаоение или деактивация "действия")
			        400 Bad Request (Ошибка верификации данных)
           
MarkController [/profile/]
  POST [{id}/mark] - создание оценки
    URL example: localhost:8081/profile/{id}/mark
		Payload: AddMarkRequest Long(input), MarkResponse(output)
		Response: 200 OK (Успешное создание оценки)
			        400 Bad Request (Ошибка верификации данных)
  GET [mark/{id}] - получение оценки по id
    URL example: localhost:8081/profile/mark/{id}
		Payload: Long(input), MarkResponse(output)
		Response: 200 OK (Успешное получение данных о оценке)
			        400 Bad Request (Ошибка верификации данных)
  GET [{id}/mark] - получение оценки по id профиля
    URL example: localhost:8081/profile/{id}/mark
		Payload: Long(input), MarkResponse(output)
		Response: 200 OK (Успешное получение данных о оценке)
			        400 Bad Request (Ошибка верификации данных)
  GET [mark/day] - получение оценок за выбранный день
    URL example: localhost:8081/profile/mark/day
		Payload: LocalDate Integer Integer(input), Collection<MarkResponse>(output)
		Response: 200 OK (Успешное получение данных об оценках)
			        400 Bad Request (Ошибка верификации данных)
  GET [mark/period] - получение оценок за выбранный период
    URL example: localhost:8081/profile/mark/period
		Payload: PeriodRequest Integer Integer(input), Collection<MarkResponse>(output)
		Response: 200 OK (Успешное получение данных об оценках)
			        400 Bad Request (Ошибка верификации данных)
  PUT [mark/{id}] - Изменение данных оценки
    URL example: localhost:8081/profile/mark/{id}
		Payload: Long UpdateMarkRequest(input), MarkResponse(output)
		Response: 200 OK (Успешное изменение данных об оценке)
			        400 Bad Request (Ошибка верификации данных)
  DELETE [mark/{id}] - Удаление данных об оценки
    URL example: localhost:8081/profile/mark/{id}
		Payload: Long(input), MarkResponse(output)
		Response: 200 OK (Успешное удаление данных об оценке)
			        400 Bad Request (Ошибка верификации данных)

StatisticController [/statistic]
Статистика по "действиям"
  GET [/action/day] - получение "действия" за день
    URL example: localhost:8081/statistic/action/day
		Payload: LocalDate Integer Integer(input), Collection<ActionResponse>(output)
		Response: 200 OK (Успешное получение данных о "действиях" за день)
			        400 Bad Request (Ошибка верификации данных)
  GET [/action/period] - получение "действия" за период
    URL example: localhost:8081/statistic/action/period
		Payload: PeriodRequest Integer Integer(input), Collection<ActionResponse>(output)
		Response: 200 OK (Успешное получение данных о "действиях" за период)
			        400 Bad Request (Ошибка верификации данных)
Статистика по работникам и их результату
  GET [/all/day] - получение данных о пользователе и результате его работы за день
    URL example: localhost:8081/statistic/all/day
		Payload: LocalDate Integer Integer(input), Collection<WorkerWithResultResponse>(output)
		Response: 200 OK (Успешное получение данных о "действиях" за день)
			        400 Bad Request (Ошибка верификации данных)
  GET [/all/period] - получение данных о пользователе и результате его работы за период
    URL example: localhost:8081/statistic/all/period
		Payload: PeriodRequest Integer Integer(input), Collection<WorkerWithResultResponse>(output)
		Response: 200 OK (Успешное получение данных о "действиях" за период)
			        400 Bad Request (Ошибка верификации данных)
Статистика по работникам
  GET [/worker/day] - получение данных о пользователях активных за день
    URL example: localhost:8081/statistic/worker/day
		Payload: LocalDate Integer Integer(input), Collection<WorkerWithResultResponse>(output)
		Response: 200 OK (Успешное получение данных о пользователях активных за день)
			        400 Bad Request (Ошибка верификации данных)
  GET [/worker/period] - получение данных о пользователях активных за период
    URL example: localhost:8081/statistic//worker/period
		Payload: PeriodRequest Integer Integer(input), Collection<WorkerWithResultResponse>(output)
		Response: 200 OK (Успешное получение данных о пользователях активных за период)
			        400 Bad Request (Ошибка верификации данных)
Статистика по продукции
  GET [/product/day] - получение данных о продуктах полученных за день
    URL example: localhost:8081/statistic/product/day
		Payload: LocalDate Integer Integer(input), Collection<WorkResultResponse>(output)
		Response: 200 OK (Успешное получение данных о продуктах за день)
			        400 Bad Request (Ошибка верификации данных)
  GET [/product/period] - получение данных о пользователях активных за период
	 </pre>
    URL example: localhost:8081/statistic/product/period
		Payload: PeriodRequest Integer Integer(input), Collection<WorkResultResponse>(output)
		Response: 200 OK (Успешное получение данных о продутках за период)
			        400 Bad Request (Ошибка верификации данных)
TaskContoller [/task]
  POST [] - создание задачи
    URL example: localhost:8081/task
		Payload: AddTaskRequest Long(input), TaskResponse(output)
		Response: 200 OK (Успешное создание задачи)
			        400 Bad Request (Ошибка верификации данных)
  GET [/{id}] - получение задачи по id
    URL example: localhost:8081/profile/mark/{id}
		Payload: Long(input), TaskResponse(output)
		Response: 200 OK (Успешное получение данных о задаче)
			        400 Bad Request (Ошибка верификации данных)
  GET [/profile/{id}] - получение задач по id профиля
    URL example: localhost:8081/profile/mark/{id}
		Payload: Long(input), Collection<TaskResponse>(output)
		Response: 200 OK (Успешное получение данных о задачах)
			        400 Bad Request (Ошибка верификации данных)
  PUT [/{id}] - изменение данных задачи
    URL example: localhost:8081/profile/mark/{id}
		Payload: Long UpdateTaskRequest(input), TaskResponse(output)
		Response: 200 OK (Успешное изменение данных о задаче)
			        400 Bad Request (Ошибка верификации данных)
  DELETE [/{id}] - удаление данных задачи
    URL example: localhost:8081/profile/mark/{id}
		Payload: Long(input), TaskResponse(output)
		Response: 200 OK (Успешное удаление данных о задаче)
			        400 Bad Request (Ошибка верификации данных)
