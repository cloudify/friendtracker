class ApiController < ApplicationController
  
  def update
    u = User.find(params[:userid])
    u.location_ln = params[:ln]
    u.location_lt = params[:lt]
    
    friends = User.find(:all)
  end
  
end
