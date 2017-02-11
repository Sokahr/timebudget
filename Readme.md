#TimeBudget

This is a time budgeting webservice. It lets you create automated repeating should-have time entries, so the amount of 
time you should have worked on the Project or for the company. I.e. You should work 40h every week for the "Big Company".

Further more the Service lets you track your time spend on a timeBudget.
The service lets you enter exceptions to the should-have entries, i.e. you were ill and you get the day off, so the 
exception to the should-have time is i.e. 8h.
The same goes for the time spend on vacation.

To help you get an overview of all the time entries, you could receive a report of all budgets and the overall sum of
workingHours to do or don't, as well as the workingHours spend on vacation or illness. 

##Use Cases

* Create a TimeBudget with a Name
* Configure the TimeBudget by adding one or more AutomationTimeItems.
    * An AutomationTimeItem holds the information how often the set hours should be added and from which date till 
    which date. If no till which date is known the dateRange is open
* Add ManualTimeItems to a Budget. ManualTimeItems contains the DateRange on which they occur, the amount of time 
  and the type of time (holiday,vacation,illness,or done)
* Get a Report over all Budgets to view the balance between the accumulated time and the added manual time.
* Get a list of all AutomatedTimeItems from  a TimeBudget to
    * Delete a AutomationTimeItem
* Get a list of all ManualTimeItems from a TimeBudget to
    * Delete a ManualTimeItem

##Domain

<!-- language: lang-none -->
    +-----------------------------------------------------------------------+
    |      TimeBudget                                                       |
    -------------------------------------------------------------------------
    | getName()                                                             |                 +---------------------+                                      
    | getAutomationTimes()                                                  |---------------> | AutomationTimeItem  |
    | addAutomationTime(AutomationTimeItem)                                 |           1-n   -----------------------
    | removeAutomationTime(AutomationTimeItme)                              | +----------------------+
    | getManualTimes()                                                      |---------------> | ManualTimeItem       |
    | addManualTime(ManualTimeItem)                                         |           1-n   ------------------------
    | removeManualTime(ManualTimeItem)                                      |             
    | getAccumulatedAutoTimeTill(LocalDate)                                 |
    | getTimeBalanceTill(LocalDate)                                         |            +------------------+
    | getManualTimeByTypeTill(TimeType, LocalDate)                          |----------->| TimeBudgetReport |
    |                                                                       |            --------------------
    -------------------------------------------------------------------------
    
    +-------------------------------------------+
    | AutomationTimeItem                        |
    ---------------------------------------------           +--------------+
    | getHours()                                |---------> | WorkingHours |
    | setHours(WorkingHours)                    |       1-1 ----------------
    | getAutomationInterval()                   |           +--------------------+
    | setAutomationInterval(AutomationInterval) |---------> | AutomationInterval |
    | getOpenDateRange()                        |       1-1 ----------------------
    | setOpenDateRange()                        |           +---------------+
    | calculateAccumulatedTimeTill(LocalDate)   |---------> | OpenDateRange |
    ---------------------------------------------       1-1 -----------------
    
    +---------------------------------+
    | ManualTimeItem                  |
    -----------------------------------           +--------------+
    | getHours()                      |---------> | WorkingHours |
    | setHours(WorkingHours)          |       1-1 ----------------
    | getTimeType()                   |           +----------+
    | setTimeType(TimeType)           |---------> | TimeType |
    | getDateRange()                  |       1-1 ------------
    | setDateRange()                  |           +-----------+
    |                                 |---------> | DateRange |
    -----------------------------------       1-1 -------------
    
    +---------------
    | WorkingHours |
    ----------------
    | getValue()   |
    ----------------
    
    +--------------------+
    | AutomationInterval |
    ----------------------
    | ONCE
    | WEEKLY
    | MONTHLY
    | MONDAYS
    | TUESDAYS
    | WEDNESDAYS
    | THURSDAYS
    | FRIDAYS
    | SATURDAYS
    | SUNDAYS           
    --------------------
    
    +---------------+
    | TimeType |
    -----------------
    | DONE
    | ILLNESS
    | VACATION
    | HOLIDAY
    -----------------
    
    +--------------------------+
    | OpenDateRange            |
    ----------------------------
    | getFromDate()            |
    | getTillDate()            |
    | isDateInRange(LocalDate) |
    ----------------------------
    
    +--------------------------+
    | DateRange                |
    ----------------------------
    | getFromDate()            |
    | getTillDate()            |
    | isDateInRange(LocalDate) |
    ----------------------------
    
### TimeBudget
 The root entity. it contains a name and a list for automationTimeItems and manualTimeItems.
 
 
### AutomationTimeItem
 Item with the automated interval Time. This time should be done on a budget. 
 
 `hours` The WorkingHours which will be accumulated for each interval.
 
 `interval` The AutomationInterval in which the Time will be accumulated
 
 `openDateRange` The openDateRange in which the accumulation will happen, if the openDateRange has no tillDate 
 the accumulation will be infinite. 
 
 `calculateAccumulatedTimeTill(LocalDate)` calculates the time which has been accumulated for the hours in the interval
 for the openDateRange till parameter the date.
  
        
### ManualTimeItem
 Item with manual entered time Entries like time done on a budget, or time on vacation budget. 
 
 `hours` WorkingHours cannot be null 
 
 `timeType` TimeType what kind of time {DONE, ILLNESS, VACATION, HOLIDAY}, cannot be null.

 `dateRange` DateRange when was the item done. Cannot be null

### WorkingHours
 Value Object for hours done working. The value must be larger then 0.
 
### OpenDateRange
 Value Object for a date-range where the `tillDate` is uncertain.

 `fromDate` the LocalDate when the range begins, including this day. It cannot be null.
  
  `tillDate` the LocalDate when the range ends, including this day. This field is optional. 
  If it is null the range is open.
  
  `boolean isDateInRange(LocalDate)` returns true if the Date is equalTo or between `fromDate` and `tillDate`.
  If the `tillDate` is null the method returns true when the date is after the `fromDate`
       
### DateRange
 Value Object for a fixed date-range.
 If `fromDate` and `tillDate`are equal the range is just for that day.
 
 `fromDate` the LocalDate when the range begins, including this day. It cannot be null.
 
 `tillDate` the LocalDate when the range ends, including this day. It cannot be null.
 
 `boolean isDateInRange(LocalDate)` returns true if the Date is EqualTo or in Between `fromDate` and `tillDate` 
 
##REST-API

#####Create a new Time-Budget:
<!-- language: lang-none -->
    POST {host}/time_budget/
    content: { "budgetName":"Budget Name"}
    content-type: JSON-UTF8
    
#####Create AutomationTimeItem for TimeBudget:
<!-- language: lang-none -->
    POST {host}/time_budget/{timeBudgetId}/automation
    content: 
<!-- language: lang-json -->
    { "hours":8, 
    "interval":DAILY,
    "beginDate":YYYY-MM-DD,
    "tillDate":YYYY-MM-DD [optional]}
<!-- language: lang-none -->            
    content-type: JSON-UTF8
    
##### Create ManualTimeItem for TimeBudget:
<!-- language: lang-none -->
    POST {host}/time_budget/{timeBudgetId}/manual
    content: { "hours":8,
                "TimeType":DONE,
                "beginDate":YYYY-MM-DD
                "tillDate":YYYY-MM-DD }
    content-type: JSON-UTF8
    
##### Get balances
 <!-- language: lang-none -->
    GET {host}/time_budget/balances?page=0&size=5
    
  result-body:
  <!-- language: lang-none -->
     {[
       {"budgetName":"xyz",
       "balance":-0.5},
       {"budgetName":"abc",
       "balance":1}
       ]}
       
 ##### Get all AutomationTimeItems for a Budget
 GET {host}/time_budget/{timeBudgetId}/automations?page=0&size=10
 ##### Get all ManualTimeItems for a Budget
 GET {host}/time_budget/{timeBudgetId}/manual?page=0&size=10&sort="beginDate"&beginDate.dir=asc
 ##### Edit TimeBudget (only name)
 PUT {host}/time_budget/{timeBudgetId}
 ##### DELETE AutomationTimeItem
 DELETE {host}/time_budget/{timeBudgetId}/automation/{automationTimeItemId}
 ##### DELETE ManualTimeItem
 DELETE {host}/time_budget/{timeBudgetId}/manual/{manualTimeItemId}
 ##### DELETE TimeBudget
 DELETE {host}/time_budget/{timeBudgetId}
 