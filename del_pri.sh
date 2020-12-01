#!/bin/bash

git filter-branch --force --index-filter 'git rm --cached -r --ignore-unmatch chat5000/src/main/resources' --prune-empty --tag-name-filter cat -- --all
git filter-branch --force --index-filter 'git rm --cached -r --ignore-unmatch contest8005/src/main/resources' --prune-empty --tag-name-filter cat -- --all
git filter-branch --force --index-filter 'git rm --cached -r --ignore-unmatch gateway82/src/main/resources' --prune-empty --tag-name-filter cat -- --all
git filter-branch --force --index-filter 'git rm --cached -r --ignore-unmatch message8600/src/main/resources' --prune-empty --tag-name-filter cat -- --all
git filter-branch --force --index-filter 'git rm --cached -r --ignore-unmatch route8700/src/main/resources' --prune-empty --tag-name-filter cat -- --all
git filter-branch --force --index-filter 'git rm --cached -r --ignore-unmatch team8500/src/main/resources' --prune-empty --tag-name-filter cat -- --all
git filter-branch --force --index-filter 'git rm --cached -r --ignore-unmatch upload3000/src/main/resources' --prune-empty --tag-name-filter cat -- --all
git filter-branch --force --index-filter 'git rm --cached -r --ignore-unmatch userManage7500/src/main/resources' --prune-empty --tag-name-filter cat -- --all
