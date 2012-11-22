function GeoIpController($scope, $http) {
   $scope.country = {name:"n.a.", code:"n.a.", remoteHost:"n.a."};

   $scope.getCountry = function () {
      $http.get('/rest/resolve').success(function (data) {
         $scope.country = data;
         var values = {};
         values[data.code] = 1;
         _worldmap.series.regions[0].setValues(values);
      });
   }
}
