'use strict';

angular.module('jeducenterApp')
    .factory('ReasonForLeaving', function ($resource, DateUtils) {
        return $resource('api/reasonForLeavings/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
