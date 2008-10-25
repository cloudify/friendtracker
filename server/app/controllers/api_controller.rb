class ApiController < ApplicationController
  
  def update
    u = User.find(params[:userid])
    u.location_ln = params[:ln]
    u.location_lt = params[:lt]
    u.location_updated_at = Time.now()
    u.save
    
    @users = User.find(:all, :conditions => [ 'id != ? AND location_updated_at > ?', u.id, 30.minutes.ago] )
    respond_to do |format|
      format.csv { 
        render :text => (@users.collect {|u| [u.id, u.login, u.location_lt, u.location_ln].join(",") }.join("\n")), :layout => false 
      }
    end
  end
  
end
