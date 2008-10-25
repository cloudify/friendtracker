class AddUserLocation < ActiveRecord::Migration
  def self.up
    add_column :user, :location_lt, :float, :default => 0
    add_column :user, :location_ln, :float, :default => 0
  end

  def self.down
    remove_column :user, :location_lt
    remove_column :user, :location_ln
  end
end
