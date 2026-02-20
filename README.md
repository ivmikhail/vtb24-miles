# vtb24-miles

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/5420977c69824cce879294432154eb58)](https://app.codacy.com/app/ivmikhail/vtb24-miles?utm_source=github.com&utm_medium=referral&utm_content=ivmikhail/vtb24-miles&utm_campaign=badger) 
[![Build Status](https://travis-ci.org/ivmikhail/vtb24-miles.svg?branch=master)](https://travis-ci.org/ivmikhail/vtb24-miles)

A small utility to calculate miles/bonus rewards for VTB24 cards. Supported cards:
 * Card of the World, Platinum/Gold https://www.vtb24.ru/cards/credit/platinum/worldcard/
 * Multicard with travel/collection/cashback option https://www.vtb24.ru/cards/multicard/

Useful for validating the bank's reward calculation.

## How to use

### System requirements

* Any OS works: Windows, Mac, etc.
* Java 8 or newer must be installed.
  * It is likely already installed, but if not, [download it here](https://www.java.com/en/download/).
  * If needed, use the [installation guide](https://www.java.com/en/download/help/download_options.xml).
  
### Usage

1. Download the latest release from https://github.com/ivmikhail/vtb24-miles/releases/.
   * You need the `*.jar` file.
1. Download your transaction statement.
   * https://online.vtb.ru > authenticate > find your card > statement > select period > "Show" > Export > CSV
   * It is recommended to export the full card usage period. You can filter by month during analysis.
   * Place the statement (usually named `statement.csv`) into the same directory as the `*.jar` file.
1. Open a terminal and navigate to that directory.
1. Run analysis with `java -jar vtb24-miles-1.5.jar -s statement.csv`. The program prints a report.
  1. To save the result as `csv`, run `java -jar vtb24-miles-1.5.jar -s statement.csv -export 1.csv`.
  1. Open `1.csv` from the same directory in Excel or LibreOffice.
  1. The jar filename may differ; `vtb24-miles-1.5.jar` is only an example.
1. To change reward rules, pass `-rule %option%`, for example `-rule MC_TRAVEL`. Supported options: `KM_PLATINUM, KM_GOLD, MC_TRAVEL, MC_CASHBACK`. By default, rewards are calculated using `KM_PLATINUM` (Card of the World Platinum).

In the example below, both the program and statement are in the `vtb24-miles` folder, and the goal is to view miles for December 2017.
 
![How to use vtb24-miles](/static/howto-v2.gif)

#### Launch options

![Launch options](/static/help-v2.png)

### FAQ

<dl>
  <dt>My card account is in USD (or EUR). How are miles calculated?</dt>
  <dd>
    The transaction amount is converted to rubles (because 1 mile = 1 RUB). The exchange rate is taken from VTB at https://www.vtb24.ru/banking/currency/rate-of-conversion/ for the operation date. Then 4% or 5% is applied to the amount in RUB.
  </dd>
  
  <dt>My card account is in USD (or EUR). Can I trust the result?</dt>
  <dd>
    In this case, the miles value is approximate. There are several reasons: bank/payment-system conversion fees and rates are not included, and the exact internal bank rate used for miles calculation is unknown.
  </dd>
  
  <dt>My card account is in RUR. Can I trust the result?</dt>
  <dd>
    In general, yes.
  </dd> 
  
  <dt>Which operations are treated as "foreign" (5% cashback)?</dt>
  <dd>Operations where transaction currency differs from account currency, and operations whose descriptions match keywords from the config file: https://github.com/ivmikhail/vtb24-miles/blob/master/src/main/resources/app.properties</dd>
    
  <dt>Which operations do not get miles?</dt>
  <dd>Transactions with amount &lt; 100 RUB and transactions whose descriptions match keywords from the config file: https://github.com/ivmikhail/vtb24-miles/blob/master/src/main/resources/app.properties</dd>
  
  <dt>Why is MCC not used to determine transaction category?</dt>
  <dd>The statement does not include MCC code.</dd>
  
  <dt>My calculated miles do not match the bank's miles</dt>
  <dd>Most likely some operations were categorized incorrectly (see the next question if you found such a case), or the bank credited miles incorrectly. In that case, submit a claim to the bank.</dd>
  
  <dt>I know operation XXX should be treated as "foreign" or ignored. What should I do?</dt>
  <dd>Download the config file (https://github.com/ivmikhail/vtb24-miles/blob/master/src/main/resources/app.properties) into the program directory, adjust it, and run the program with that file. Example: "java -jar vtb24-miles.jar -s statement.csv -p app.properties". If possible, send your updated file to ivmikhail at gmail com so it can be added for everyone.</dd>
  
  <dt>How can I calculate miles for 2 or more cards?</dt>
  <dd>Download a separate statement for each card (see step 2), then run:  
 "java -jar vtb24-miles.jar -s statement.csv -s statement2.csv"</dd>
  
  <dt>I see a large difference for operations before July 1, 2017</dt>
  <dd>Rules were different at that time. VTB converted old points to the new format, apparently not per transaction, which may cause differences. To calculate old operations, use the older version: https://github.com/ivmikhail/vtb24-miles/releases/tag/vtb24-miles-1.1</dd>
 
  <dt>I want to improve the program/code. What should I do?</dt>
  <dd>https://git-scm.com/book/en/v2/GitHub-Contributing-to-a-Project</dd>
  
  <dt>If you have a suggestion/question or found a bug</dt>
  <dd>Email ivmikhail at gmail com, or better create an issue: https://github.com/ivmikhail/vtb24-miles/issues/</dd>
</dl>

### License

```
Copyright (c) 2017, Mikhail Ivanov <ivmikhail at gmail com>

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice,
      this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
```
