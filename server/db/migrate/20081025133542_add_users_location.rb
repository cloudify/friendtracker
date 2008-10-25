class AddUsersLocation < ActiveRecord::Migration
  def self.up
    add_column :users, :location_lt, :float, :default => 0
    add_column :users, :location_ln, :float, :default => 0
  end

  def self.down
    remove_column :users, :location_lt
    remove_column :users, :location_ln
  end
end
