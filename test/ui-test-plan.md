# SlotBot UI test plan

Run the regression case with the project-local `test-ui` skill. The complete
expected output is stored in `text-ui-test/EXPECTED.TXT` and the commands are
stored in `text-ui-test/input.txt`.

## Case 1: Add each task type and handle basic errors

Aim: Verify that invalid todo, deadline, event, and unknown commands show
specific errors without adding tasks, while valid task commands create the
correct task types, show the task count, handle invalid delete inputs safely,
delete a task and renumber the list, and share task-number validation across
mark, unmark, and delete.

The regression runner uses a fresh temporary working directory, so the case
also verifies that a missing save file does not prevent first launch and that
the relative `data/slotbot.txt` file is created without touching user data.

Inputs:

```text
todo
blah
deadline
deadline return book
event
event project meeting
event project meeting /from Mon 2pm
todo borrow book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
todo join sports club
list
delete
delete abc
delete 99
delete 2
list
mark
unmark
mark 2
unmark 2
mark abc
unmark abc
mark 99
bye
```

Expected output:

See `text-ui-test/EXPECTED.TXT`.

## Case 2: Handle save-file errors

Aim: Verify that SlotBot starts with an empty list when it cannot read the
save file, and skips malformed task records while loading other valid records.

Expected warnings:

```text
Warning: Unable to load saved tasks.
Starting with an empty list.
```

```text
Warning: Ignoring invalid task data on line NUMBER.
```

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
