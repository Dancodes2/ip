# SlotBot UI test plan

Run the regression case with the project-local `test-ui` skill. The complete
expected output is stored in `text-ui-test/EXPECTED.TXT` and the commands are
stored in `text-ui-test/input.txt`.

## Case 1: Add each task type

Aim: Verify that todo, deadline, and event commands create the correct task
types, show the task count, and handle invalid mark inputs safely.

Inputs:

```text
todo borrow book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
list
mark 2
unmark 2
mark abc
mark 99
bye
```

Expected output:

See `text-ui-test/EXPECTED.TXT`.

## Maintaining the regression test

1. Edit the Java code.
2. Update `text-ui-test/input.txt` only when adding or changing a scenario.
3. Run `text-ui-test/runtest.bat`.
4. Inspect `text-ui-test/ACTUAL.TXT`.
5. Fix the code and rerun if the output is unexpected.
6. Copy `ACTUAL.TXT` to `EXPECTED.TXT` only after confirming the output is
   correct and intentional.
7. Rerun the test and confirm that it passes.

Do not replace `EXPECTED.TXT` without reviewing `ACTUAL.TXT` first.
