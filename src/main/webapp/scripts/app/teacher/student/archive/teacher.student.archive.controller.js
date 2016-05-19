'use strict';

angular.module('jeducenterApp')
    .controller('TeacherStudentArchiveController', function ($scope, $state, Student, tmhDynamicLocale,
                                                             i18nService, $translate, uiGridConstants, LearningType) {
        $scope.learningTypes = [];
        LearningType.query(function (result) {
            result.forEach(function (item) {
                $scope.learningTypes.push({value: item.id, label: item.name})
            });
        });

        $scope.loadAll = function (predicate) {
            Student.query(predicate, function (result) {
                $scope.studentsGrid.data = result;
            });
        };

        $scope.loadAll();

        /*Localization*/
        i18nService.setCurrentLang(tmhDynamicLocale.get());

        $scope.studentsGrid = {
            enableGridMenu: true,
            enableColumnResizing: true,
            gridMenuTitleFilter: $translate,
            enableFiltering: true,
            useExternalFiltering: true,
            columnDefs: [
                {
                    displayName: 'jeducenterApp.student.lastName', field: 'lastName', width: '12%',
                    headerCellFilter: "translate"
                },
                {
                    displayName: 'jeducenterApp.student.firstName', field: 'firstName', width: '10%',
                    headerCellFilter: "translate"
                },
                {
                    displayName: 'jeducenterApp.student.middleName', field: 'middleName', width: '12%',
                    headerCellFilter: "translate"
                },
                {
                    displayName: 'jeducenterApp.student.type', field: 'learningType.name', width: '6%',
                    headerCellFilter: "translate",
                    filter: {
                        type: uiGridConstants.filter.SELECT,
                        selectOptions: $scope.learningTypes
                    }
                },
                {
                    displayName: 'jeducenterApp.student.email', field: 'email', width: '14%',
                    headerCellFilter: "translate"
                },
                {
                    displayName: 'jeducenterApp.student.phone', field: 'phone', width: '11%',
                    headerCellFilter: "translate"
                },
                {
                    displayName: 'jeducenterApp.student.university', field: 'university', width: '10%',
                    headerCellFilter: "translate",
                    filter: {
                        type: uiGridConstants.filter.SELECT,
                        selectOptions: [{value: 'СГУ', label: 'СГУ'}, {value: 'СГТУ', label:'СГТУ'}]
                    }
                },
                {
                    displayName: 'jeducenterApp.student.specialty', field: 'specialty', width: '12%', visible: false,
                    headerCellFilter: "translate"
                },
                {
                    displayName: 'jeducenterApp.student.faculty', field: 'faculty', width: '12%', visible: false,
                    headerCellFilter: "translate"
                },
                {
                    displayName: 'jeducenterApp.student.course', field: 'course', width: '4%', visible: false,
                    headerCellFilter: "translate"
                },
                {
                    displayName: 'jeducenterApp.student.isActive', field: 'isActive', width: '8%', type: 'boolean',
                    visible: false, headerCellFilter: "translate",
                    filter: {
                        type: uiGridConstants.filter.SELECT,
                        selectOptions: [{value: 'true', label: 'true'}, {value: 'false', label:'false'}]
                    }
                },
                {
                    displayName: 'jeducenterApp.student.groupOfStudent', name: 'groupOfStudent', width: '8%',
                    visible: false, enableFiltering: false, headerCellFilter: "translate",
                    cellTemplate: 'scripts/app/teacher/student/ui-grid/student.group.cell.html'
                },
                {
                    displayName: 'jeducenterApp.student.curator', name: 'curator', width: '8%',
                    visible: false, enableFiltering: false, headerCellFilter: "translate",
                    cellTemplate: 'scripts/app/teacher/student/ui-grid/student.curator.cell.html'
                },
                {
                    name: ' ', width: '14%', enableSorting: false, enableFiltering: false,
                    cellTemplate: 'scripts/app/teacher/student/archive/ui-grid/student.archive.buttons.html'
                }
            ],
            onRegisterApi: function (gridApi) {
                $scope.gridApi = gridApi;
                $scope.gridApi.core.on.filterChanged($scope, function () {
                    var grid = this.grid;
                    var predicate = {};
                    $.extend(predicate, {lastName: grid.columns[0].filters[0].term});
                    $.extend(predicate, {firstName: grid.columns[1].filters[0].term});
                    $.extend(predicate, {middleName: grid.columns[2].filters[0].term});
                    $.extend(predicate, {learningType: grid.columns[3].filters[0].term});
                    $.extend(predicate, {email: grid.columns[4].filters[0].term});
                    $.extend(predicate, {phone: grid.columns[5].filters[0].term});
                    $.extend(predicate, {university: grid.columns[6].filters[0].term});
                    $.extend(predicate, {specialty: grid.columns[7].filters[0].term});
                    $.extend(predicate, {faculty: grid.columns[8].filters[0].term});
                    $.extend(predicate, {course: grid.columns[9].filters[0].term});
                    $.extend(predicate, {isActive: grid.columns[10].filters[0].term});
                    $scope.loadAll(predicate);
                });
            }
        };

        $scope.refresh = function () {
            $scope.loadAll();
        };
    });
