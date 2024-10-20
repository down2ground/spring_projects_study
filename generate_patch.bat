@echo off

if [%1]==[] (
    echo Commit hash is not specified
    pause
    exit /b
)

set OUT=doc_src\patches\%1.txt

echo ^<!--VARIABLES {"title": "Patch %1"}--^> > %OUT%
echo. >> %OUT%
echo ^<strong^>Commit message:^</strong^> >> %OUT%
call git log --format=%%%%B -n 1 %1 >> %OUT%
echo. >> %OUT%
git diff %1^^! | python "%GIT_DIFF_2_HTML_HOME%\gitDiff2Html.py" >> %OUT%
