TODO
====

Common
------

- Display snackbar on error when doing async api calls

App
---

- Add a way for user to say they've tested positive
- Add notifications systems
- Add event creation capabilities (edit/deletion left to admin only?)
- Add location creation capabilities (edit/deletion left to admin only?)

- Add validation
    - When creating event: startdate < enddate
    - When creating place, gps coordinates should match the following regex: `^[-+]?([1-8]?\d(\.\d+)?|90(\.0+)?),\s*[-+]?(180(\.0+)?|((1[0-7]\d)|([1-9]?\d))(\.\d+)?)$`

Dashboard
---------

- Add user create/edit/delete capabilities 
- Add event delete capabilities 
- Add location delete capabilities 

- Add validation
  - When creating event: startdate < enddate
  - When creating place, gps coordinates should match the following regex: `^[-+]?([1-8]?\d(\.\d+)?|90(\.0+)?),\s*[-+]?(180(\.0+)?|((1[0-7]\d)|([1-9]?\d))(\.\d+)?)$`
  
Bonus
-----

- Add statistics on infection numbers in dashboard (using basic cards)