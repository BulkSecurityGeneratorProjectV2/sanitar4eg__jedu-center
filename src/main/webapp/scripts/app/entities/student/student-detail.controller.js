'use strict';

angular.module('jeducenterApp')
    .controller('StudentDetailController', function ($scope, $rootScope, $stateParams, entity, Student) {
        $scope.student = entity;
        $scope.load = function (id) {
            Student.get({id: id}, function(result) {
                $scope.student = result;
            });
        };
        var unsubscribe = $rootScope.$on('jeducenterApp:studentUpdate', function(event, result) {
            $scope.student = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
