angular.module('appServices', [])

    .factory('messageService', ['$http', '$q', '$log', function($http, $q, $log) {


        var service = {

            socketTypes : {
                cdi : '/socket/echo-message',
                rx : '/socket/rx-echo-message'
            },

            send : function(message) {
                return $http.post('rest/message', {message : message});
            },

            openSocket : function(socketType) {
                var socket = new WebSocket(getRootUri() + (this.socketTypes[socketType] || this.socketTypes['cdi']));
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
                        (document.location.port == "" ? ":8000" : ":" + document.location.port);
                }

                return deferred.promise;
            }
        };

        return service;

    }]);
