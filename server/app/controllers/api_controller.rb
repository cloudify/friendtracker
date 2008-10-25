class ApiController < ApplicationController
  
  def update
    u = User.find(params[:userid])
    u.location_ln = params[:ln]
    u.location_lt = params[:lt]
    u.save
    
    @users = User.find(:all)
    render :text => (@users.collect {|u| [u.id, u.login, u.location_lt, u.location_ln].join(" ") }.join("\n")), :layout => false
  end
  
end
