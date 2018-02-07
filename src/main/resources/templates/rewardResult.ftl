<#setting number_format="###.##">

<#macro transactions transactionsList>
СЧЕТ                ДАТА ОБР     ОПИСАНИЕ                   СУММА       ВАЛЮТА МИЛИ
------------------------------------------------------------------------------------
    <#if transactionsList??>
        <#list transactionsList as var>
${var.transaction.accountNumberMasked}   ${var.transaction.processedDate}   ${var.transaction.description?right_pad(25)}  ${var.transaction.amountInAccountCurrency?right_pad(10)}  ${var.transaction.accountCurrencyCode?right_pad(4)}   ${var.miles}
        </#list>
    <#else>
        <нет операций>
    </#if>
</#macro>

Операции с кэшбеком 4%

<@transactions reward?api.getTransactions("WITHDRAW_NORMAL") />



Операции с кэшбеком 5%

<@transactions reward?api.getTransactions("WITHDRAW_FOREIGN") />



Пополнения

<@transactions reward?api.getTransactions("REFILL") />



Операции, кешбэк за которые не положен

<@transactions reward?api.getTransactions("WITHDRAW_IGNORE") />


Период с ${settings.minDate} с ${settings.maxDate}

Всего миль получено      ${reward.totalRewardMiles}
Всего пополнений, в руб  ${reward.totalRefillRUR}
Всего списаний  , в руб ${reward.totalWithdrawRUR}
Эффективный кэшбек %     ${reward.effectiveCashback}