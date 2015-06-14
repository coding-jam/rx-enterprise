angular.module('appServices', [])

    .factory('messageService', ['$http', '$q', '$log', function($http, $q, $log) {


        var service = {

            send : function(message) {
                return $http.post('rest/message', {message : message});
            },

            openSocket : function() {
                var socket = new WebSocket(getRootUri() + '/socket/echo-message');
                var deferred = $q.defer();
                socket.onopen = function(event) {
                    $log.log(event);
                    deferred.resolve(socket);
                };
                socket.onerror = function(event) {
                    $log.log(event);
                    socket.onerror = function(event) {
                        alert(event);
                    };
                    deferred.reject(event);
                };

                socket.onclose = function(event) {
                    $log.log(event);
                };

                function getRootUri() {
                    return "ws://" + (document.location.hostname == "" ? "localhost" : document.location.hostname) +
                        (document.location.port == "" ? "" : ":" + document.location.port);
                }

                return deferred.promise;
            }
        };

        return service;

    }]);
