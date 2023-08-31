# restfulAPI
Бэкенд веб приложения для тестового задания.
Планировщик задач.
Эндпоинты:
* Post http://address:port/users - добавить нового пользователя
* Patch http://address:port/users - изменить данные пользователя
* Get http://address:port/users - получить данные пользователя
* Get http://address:port/users/all - получить список всех пользователей

* Post http://address:port/tasks - добавить задачу
* Patch http://address:port/tasks/{taskId} - изменить данные задачи
* Patch http://address:port/tasks/{taskId}/{status} - изменить статус задачи
* Get http://address:port/tasks/{taskId} - получить данные о задаче
* Get http://address:port/tasks - получить список задач пользователя по фильтрам, указанных в параметрах*
* Delete http://address:port/tasks/{taskId} - удалить задачу

  *Параметры фильтрации:
  - query - совпадение текста с названием задачи или ее описанием
  - status - фильтр статуса задачи
  - startDate & endDate - временной промежуток задачи (указываются оба параметра)
  - page & size - вывод данных в виде страницы (по умолчанию 0 и 10 соответсвенно)
 
  Для всех эндпоинтов http://address:port/tasks указывается идентификатор автора задачи в заголовке "user-id".
