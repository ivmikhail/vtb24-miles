# vtb24-miles

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/5420977c69824cce879294432154eb58)](https://app.codacy.com/app/ivmikhail/vtb24-miles?utm_source=github.com&utm_medium=referral&utm_content=ivmikhail/vtb24-miles&utm_campaign=badger) 
[![Build Status](https://travis-ci.org/ivmikhail/vtb24-miles.svg?branch=master)](https://travis-ci.org/ivmikhail/vtb24-miles)

Приспособа для подсчета мильного вознаграждения Карты Мира(Платинум) ВТБ24 https://www.vtb24.ru/cards/credit/platinum/worldcard/

Полезно для сверки с расчетом Банка

## Как использовать

### Системные требования

* Подойдет любая ОС: Windows, Mac и т.п
* У вас должна быть установлена Java 8 и выше
  * Скорее всего, она у вас уже установлена. Но если нет, [скачайте отсюда](https://www.java.com/ru/download/)
  * [Подробная инструкция по установке](https://www.java.com/ru/download/help/download_options.xml), если у вас возникли трудности
  
### Как пользоваться

* Скачайте последнюю версию программы здесь - https://github.com/ivmikhail/vtb24-miles/releases/
  * Нужен `*.jar` файл
* Скачайте выписку по операциям
  * https://online.vtb.ru > аутентифицируйтесь > найдите нужную карту > выписка > выберите период > "Показать" > Экспорт > CSV
  * Рекомендую выбрать весь период использования карты. При анализе выписки, программе можно указать месяц, в котором проведены операции
  * Положите выписку (обычно называется `statement.csv`) в ту директорию, где находится программа
* Откройте терминал, перейдите в директорию где лежит выписка и программа
* Запустите программу с соответствующими параметрами запуска
... add screenshot here...
* Программа выведет отчет
... add screenshot here...

#### Параметры запуска

... coming soon ...

### Вопросы и ответы

<dl>
  <dt>Счет карты у меня USD(или EUR), как считаются мили?</dt>
  <dd>
    Сумма списания переводится в рубли(т.к 1 миля = 1 руб). Курс берется с сайта ВТБ https://www.vtb24.ru/banking/currency/rate-of-conversion/, на дату проведения операции. От суммы списания в рублях берется 4% или 5%
  </dd>
  
  <dt>Счет карты у меня USD(или EUR), можно ли верить программе насчет миль?</dt>
  <dd>
    В этом случае количество миль будет примерным. Есть нескольким причин: не учитывается комиссия(и/или курс) банка(и/или платежной системы) при конвертации, неизвестно, какой курс использует банк для подсчета миль
  </dd>
  
  <dt>Счет карты у меня RUR, можно ли верить программе насчет миль?</dt>
  <dd>
    По идее, да
  </dd> 
  
  <dt>Какие операции попадают в "заграничные"(кешбэк 5%)?</dt>
  <dd>Те, у которых валюта списания не совпадает с валютой счета и те, в описании которых встречаются слова заданные в конфигурационном файле - https://github.com/ivmikhail/vtb24-miles/blob/master/src/main/resources/app.properties</dd>
    
  <dt>На какие операции не начисляются мили?</dt>
  <dd>Сумма списания  < 100 руб и те, в описании которых встречаются слова заданные в конфигурационном файле - https://github.com/ivmikhail/vtb24-miles/blob/master/src/main/resources/app.properties</dd>
  
  <dt>Почему не используется MCC для определения категории операции?</dt>
  <dd>MCC код операции отсутствует в выписке</dd>
  
  <dt>У меня количество миль не совпадает с тем, что начислил банк</dt>
  <dd>Скорее всего, операции попадают не в ту категорию, проверьте(если выявили проблему см. след. вопрос) или Банк неправильно начислил мили. БИНГО! Пишите претензию в БАНК, а мне благодарность - ivmikhail собака gmail com</dd>
  
  <dt>Я знаю что операция XXX должна быть как "заграничная"/игнорирована. Что мне делать?</dt>
  <dd>Скачайте конфигурацию(https://github.com/ivmikhail/vtb24-miles/blob/master/src/main/resources/app.properties) в директорию программы, измените как нужно, и запустите программу указав конф. файл. Пример: "java -jar vtb24-miles.jar -s statement.csv -p app.properties". Если не трудно, пришлите свой измененный файл мне ivmikhail собака gmail com, я добавлю ваши настройки для всех</dd>
  
  <dt>Я хочу улучшить программу(код), что мне делать?</dt>
  <dd>https://git-scm.com/book/en/v2/GitHub-Contributing-to-a-Project</dd>
  
  <dt>Если у вас есть предложение/вопрос или вы нашли ошибку</dt>
  <dd>Пишите на почту ivmikhail собака gmail com, а еще лучше сюда https://github.com/ivmikhail/vtb24-miles/issues/new</dd>
</dl>

### License
... coming soon ...
